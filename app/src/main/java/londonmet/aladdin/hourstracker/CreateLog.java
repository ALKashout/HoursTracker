package londonmet.aladdin.hourstracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.Hours;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CreateLog extends ActionBarActivity {

    private LogOperation logOperation;

    private int hour;
    private int minute;
    private int year, month, day;
    TextView startText, endText, dateText;

    static final int TIME_DIALOG_01 = 999;
    static final int TIME_DIALOG_02 = 888;
    static final int DATE_DIALOG_ID = 777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_log);

        logOperation = new LogOperation(this);
        logOperation.open();

        startText = (TextView) findViewById(R.id.startLogText);
        endText = (TextView) findViewById(R.id.endLogText);
        dateText = (TextView) findViewById(R.id.dateTextView);

        Button startButton = (Button) findViewById(R.id.startLogButton);
        Button endButton = (Button) findViewById(R.id.endLogButton);
        Button dateButton = (Button) findViewById(R.id.dateLogButton);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_01);
            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_02);
            }
        });

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        ListView allLogs = (ListView) findViewById(R.id.logList);
        final List<Log> values = logOperation.getAlllogs();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, values);
        allLogs.setAdapter(adapter);
        allLogs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log selectedItem = (Log) parent.getItemAtPosition(position);
                String total = selectedItem.getLogTotal();
                Toast.makeText(getApplicationContext(), total, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addLog(View view) throws ParseException {
        startText = (TextView) findViewById(R.id.startLogText);
        endText = (TextView) findViewById(R.id.endLogText);
        dateText = (TextView) findViewById(R.id.dateTextView);
        String startLog = startText.getText().toString();
        String endLog = endText.getText().toString();
        String dateLog = dateText.getText().toString();
        String dateStart = dateLog + " " + startLog;
        String dateStop = dateLog + " " + endLog;
        String total;

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Date d1 = null;
        Date d2 = null;

        d1 = format.parse(dateStart);
        d2 = format.parse(dateStop);

        DateTime dt1 = new DateTime(d1);
        DateTime dt2 = new DateTime(d2);

        int diffTimeH = Hours.hoursBetween(dt1, dt2).getHours() % 24;
        double totalHours = diffTimeH * 6.63;

        String temptotal = String.valueOf(totalHours);
        total = temptotal;

        Log log = logOperation.addLog(startLog, endLog, dateLog, total);

        Toast.makeText(this, log.toString(), Toast.LENGTH_LONG).show();
    }


    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_01:
                return new TimePickerDialog(this, timePickerListener1, hour, minute, false);
            case TIME_DIALOG_02:
                return new TimePickerDialog(this, timePickerListener2, hour, minute, false);
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, datePickerDialog, year, month, day);
        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener1 = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;

            startText.setText(new StringBuilder().append(pad(hour)).append(":").append(pad(minute)));
            String startTime = new StringBuilder().append(pad(hour)).append(":").append(pad(minute)).toString();
            Toast.makeText(getBaseContext(), startTime, Toast.LENGTH_LONG).show();
        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener2 = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;

            endText.setText(new StringBuilder().append(pad(hour)).append(":").append(pad(minute)));
            String endTime = new StringBuilder().append(pad(hour)).append(":").append(pad(minute)).toString();
            Toast.makeText(getBaseContext(), endTime, Toast.LENGTH_LONG).show();
        }
    };

    private DatePickerDialog.OnDateSetListener datePickerDialog = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            int dateYear = year;
            int dateMonth = monthOfYear;
            int dateDay = dayOfMonth;
            //dateText.setText(new StringBuilder().append(dateDay).append("/").append(dateMonth).append("/").append(dateYear));
            dateText.setText(new StringBuilder().append(dateMonth).append("/").append(dateDay).append("/").append(dateYear));
            String date = new StringBuilder().append(dateMonth).append("/").append(dateDay).append("/").append(dateYear).toString();
            Toast.makeText(getBaseContext(), date, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.logList) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle("Choose Action");
            String[] menuItems = getResources().getStringArray(R.array.menuItems);
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_log, menu);
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

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
}
