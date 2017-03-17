package londonmet.aladdin.hourstracker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileStatus;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;
import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;


public class MainActivity extends ActionBarActivity  {

    CreatePdf createPdf;
    final String inFileName = "/data/data/londonmet.aladdin.hourstracker/databases/trackdb.db";
    File dbFile = new File(inFileName);
    DbxAccountManager mDbxAcctMgr;
    private static final String APP_KEY = "4s5yo33u2xh8del";
    private static final String APP_SECRET = "4j7ypu0rhw5l9wx";
    static final int REQUEST_LINK_TO_DBX = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createPdf = new CreatePdf(this);
        createPdf.open();
        mDbxAcctMgr = DbxAccountManager.getInstance(getApplicationContext(), APP_KEY, APP_SECRET);
    }

    public void onClickLinkToDropbox(MainActivity view) {
        mDbxAcctMgr.startLink((Activity) this, REQUEST_LINK_TO_DBX);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDbxAcctMgr.hasLinkedAccount()) {
            doDropboxTest();
        } else {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LINK_TO_DBX) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Connection Ok", Toast.LENGTH_SHORT).show();
                doDropboxTest();
            } else {
                Toast.makeText(getApplicationContext(), "Auth Failed", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void doDropboxTest() {
        try {
            final String TEST_FILE_NAME = "trackdb.db";
            DbxPath testPath = new DbxPath(DbxPath.ROOT, TEST_FILE_NAME);

            // Create DbxFileSystem for synchronized file access.
            DbxFileSystem dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());

            // Print the contents of the root folder.  This will block until we can
            // sync metadata the first time.
            List<DbxFileInfo> infos = dbxFs.listFolder(DbxPath.ROOT);

            // Create a test file only if it doesn't already exist.
            if (!dbxFs.exists(testPath)) {
                DbxFile testFile = dbxFs.create(testPath);
                try {
                    testFile.writeFromExistingFile(dbFile, true);

                    DbxFileStatus status = testFile.getSyncStatus();
                    if (!status.isCached) {
                        testFile.addListener(new DbxFile.Listener() {
                            @Override
                            public void onFileChange(DbxFile file) {
                                // Check testFile.getSyncStatus() and read if it's ready
                            }
                        });
                        // Check if testFile.getSyncStatus() is ready already to ensure nothing
                        // was missed while adding the listener
                    }

                } finally {
                    testFile.close();
                }
                Toast.makeText(getApplicationContext(), "File Created", Toast.LENGTH_SHORT).show();
            }

        }
        catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Dropbox Sync Failed", Toast.LENGTH_SHORT).show();
        }
    }



    public void createJob(View view) {
        Intent intent = new Intent(MainActivity.this, CreateJob.class);
        startActivity(intent);
    }

    public void showJob(View view) {
        Intent intent = new Intent(MainActivity.this, ListJobs.class);
        startActivity(intent);
    }

    public void createPdf(View view) throws FileNotFoundException, DocumentException {
        createPdf.createPdf();
        Toast.makeText(getApplicationContext(), "PDF file created", Toast.LENGTH_LONG).show();
    }

    public void runAboutDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.about);
        dialog.setTitle("About");

        Button dialogButton = (Button) dialog.findViewById(R.id.aboutBtn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
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
            runAboutDialog();
            return true;
        } else if (id == R.id.action_dropbox) {
            onClickLinkToDropbox(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
