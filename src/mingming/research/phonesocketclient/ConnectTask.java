package mingming.research.phonesocketclient;

import android.os.AsyncTask;
import android.util.Log;

public class ConnectTask extends AsyncTask {


protected void onProgressUpdate(String... values) {
	super.onProgressUpdate(values);
	Log.i("onProgressUpdate",values[0]);

}

@Override
protected Object doInBackground(Object... message) {
	// TODO Auto-generated method stub
	TcpClient mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
		@Override
		//here the messageReceived method is implemented
		public void messageReceived(Object... message) {
			//this method calls the onProgressUpdate
			publishProgress(message);
			Log.i("Debug","Input message: " + message);
		}
	});
	mTcpClient.run();
	
	return null;
}


}