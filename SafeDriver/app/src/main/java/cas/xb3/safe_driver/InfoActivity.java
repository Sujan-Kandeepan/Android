package cas.xb3.safe_driver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * Class which displays user guide and returns to map once finished.
 */
public class InfoActivity extends AppCompatActivity {

    /**
     * Bring UI focus to user guide and as map runs in background.
     * @param savedInstanceState State of map interface saved.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
    }

    /**
     * Return to map interface after user finishes reading.
     * @param view UI element which triggers the function.
     */
    public void finish(View view) {
        finish();
    }
}
