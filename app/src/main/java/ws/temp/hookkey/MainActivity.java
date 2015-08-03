package ws.temp.hookkey;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends ActionBarActivity {
    public static final String CALL_NUM = "CALL_NUM";
    public static final String KEY_NUM = "KEY_NUM";
    private static final String TAG = MainActivity.class.getSimpleName();
    @ViewById(R.id.call_num)
    public EditText call_num;
    @ViewById(R.id.key_num)
    public EditText key_num;
    @ViewById(R.id.toggle_hook)
    public ToggleButton toggle_hook;
    @ViewById(R.id.toggle_enable)
    public ToggleButton toggle_enable;
    public boolean isEdiable = false;
    public boolean isEnable = true;

    public SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("main", MODE_PRIVATE);
    }

    @AfterViews
    public void afterViews() {
        call_num.setText(preferences.getString(CALL_NUM, ""));
        key_num.setText(preferences.getString(KEY_NUM, ""));

        toggle_hook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isEdiable = isChecked;
            }
        });

        toggle_enable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isEnable = isChecked;
            }
        });

        toggle_hook.setChecked(false);
        toggle_enable.setChecked(true);
    }

    @Click(R.id.call_button)
    public void call() {
        if (isEnable)
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + call_num.getText().toString())));
        else
            Toast.makeText(this, "コールするにはEnableToggleをONにしてください", Toast.LENGTH_SHORT).show();
    }

    @Click(R.id.save_call_num)
    public void save_num() {
        preferences.edit()
            .putString(CALL_NUM, String.valueOf(call_num.getText()))
            .apply();
        Toast.makeText(this, "保存しました", Toast.LENGTH_LONG).show();
    }

    @Click(R.id.save_hook_key)
    public void save_key() {
        preferences.edit()
            .putString(KEY_NUM, String.valueOf(key_num.getText()))
            .apply();
        Toast.makeText(this, "保存しました", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP)
            if (isEdiable) {
                Log.d(TAG, "hook: " + event.getKeyCode());
                key_num.setText(String.valueOf(event.getKeyCode()));
            } else
                fire(event.getKeyCode());
        return false;
    }

    public void fire(int keyCode) {
        if (key_num.getText().toString().equals(String.valueOf(keyCode))) {
            Log.d(TAG, "発火");
            call();
        } else
            Log.d(TAG, "not 発火");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
