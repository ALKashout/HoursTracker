package londonmet.aladdin.hourstracker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;


public class ListJobs extends ActionBarActivity {

    private JobOperation jobOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_jobs);

        jobOperations = new JobOperation(this);
        jobOperations.open();

        ListView allJobs = (ListView) findViewById(R.id.list);

        final List<Job> values = jobOperations.getAllJobs();

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, values);

        allJobs.setAdapter(adapter);

        allJobs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Job selectedItem = (Job) parent.getItemAtPosition(position);
                String jobName = selectedItem.getJobName();
                Toast.makeText(getApplicationContext(), jobName, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getBaseContext(), CreateLog.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_jobs, menu);
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
