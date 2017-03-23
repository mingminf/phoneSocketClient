package mingming.research.phonesocketclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener {

	public EditText et_receiveddata;
	Button bt_connect;
	
	
	public static String serverIP = "100.64.179.147";
	public static int serverPort = 6688;
	public static String result_from_Server = "result";

	
	private Handler customHandler = new Handler();

	private boolean start = true;
	
	TcpClient mTcpClient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		et_receiveddata = (EditText) findViewById(R.id.editText_receiveddata);
		bt_connect = (Button) findViewById(R.id.button_connect);
		bt_connect.setOnClickListener(this);
		et_receiveddata.setText("waiting");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
			et_receiveddata.setText("before creating socket");
			
			//customHandler.postDelayed(updateTimerThread, 0);

			if(start)
			{
				start = false;
				new ConnectTask().execute("");
			}
			else
			{
				start = true;
				if(TcpClient.isConnected)
				{
					mTcpClient.stopClient();
				}
			}
			
			
			//new AsyncAction().execute();

			et_receiveddata.setText("after creating socket");
			
			
	}
	
	

    private class AsyncAction extends AsyncTask<String, Void, String> {
		Socket socket = null;
		PrintWriter out = null;
		BufferedReader in = null;
    	
    	protected String doInBackground(String... args) { 
            try {
                InetAddress serverAddr = InetAddress.getByName(serverIP);
                socket = new Socket(serverAddr, serverPort);
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
    		
            try {
            	out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
    			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    		    while(!in.ready());
    		    while(in.ready())
    		    {
    		    	result_from_Server = in.readLine();
    		    }
    			
            } catch (IOException e) {}

        return null;//returns what you want to pass to the onPostExecute()
    }

    protected void onPostExecute(String result) {
    	//et_receiveddata.setText(result_from_Server);
    	}
    } 
    
    
    private Runnable updateTimerThread = new Runnable() {
		public void run() {
			et_receiveddata.setText(result_from_Server);
			customHandler.postDelayed(this, 0);
		}
	};
    
	
	
	
	public class ConnectTask extends AsyncTask {


		protected void onProgressUpdate(Object... values) {
			super.onProgressUpdate(values);
			//EditText et = (EditText)findViewById(R.id.editText_receiveddata);
			et_receiveddata.setText(values[0].toString());			
			//Log.i("onProgressUpdate",values[0].toString());
			
		}

		@Override
		protected Object doInBackground(Object... message) {
			// TODO Auto-generated method stub
			mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
				@Override
				//here the messageReceived method is implemented
				public void messageReceived(Object... message) {
					//this method calls the onProgressUpdate
					publishProgress(message);
					//Log.i("Debug","Input message: " + message);
				}
			});
			
			mTcpClient.run();
			
			return null;
		}


		}
    
}
