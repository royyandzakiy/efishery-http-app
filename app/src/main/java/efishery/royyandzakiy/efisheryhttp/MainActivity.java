package efishery.royyandzakiy.efisheryhttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import efishery.royyandzakiy.efisheryhttp.Stopwatch;

public class MainActivity extends AppCompatActivity {

    Button send;
    TextView packetLossValue, responseTimeValue, cpuProcessingValue, payloadSizeValue, responseSuccessValue, responseFailValue, status;
    boolean clicked = false;
    int countSuccess = 0, countFail = 0, countRequest = 150;

    Stopwatch stopwatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVariables();

        stopwatch = new Stopwatch();

        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!clicked) {
                    clicked = !clicked;
                    Toast.makeText(getApplicationContext(), "requesting...", Toast.LENGTH_SHORT).show();
                    status.setText("requesting...");
                    stopwatch.start();

                    // send HTTP Request
                    for (int i=0; i<countRequest; i++) {
                        sendHttpRequest();
                    }

                    // calculate: packet loss, response time, cpu processing, payload size
                    // change values
                    packetLossValue.setText("clicked");
                    responseTimeValue.setText("clicked");
                    cpuProcessingValue.setText("clicked");
                    payloadSizeValue.setText("clicked");
                }
            }
        });
    }

    protected void sendHttpRequest() {
        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://sakernas-api.herokuapp.com/pemutakhiran/5a8239f92cece14688a14f2c";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display response
                        // responseSuccessValue.setText(response);
                        responseSuccessValue.setText("Success count: "+ ++countSuccess);
                        queue.stop();

                        requestDone();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // responseSuccessValue.setText("That didn't work!");
                responseFailValue.setText("Fail count: "+ ++countFail);
                queue.stop();

                requestDone();
            }
        });

    // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    protected void requestDone() {
        if ((countFail + countSuccess) == countRequest) {
            String elapsedTime = String.valueOf(stopwatch.getElapsedTimeSecs());
            String toastText = countRequest + " response done! " + elapsedTime  + " secs";
            Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT).show();
            status.setText("Response done!");

            packetLossValue.setText(String.valueOf(countFail) + " packets");
            responseTimeValue.setText(String.valueOf(elapsedTime) + " secs");
            // cpuProcessingValue.setText();
            // payloadSizeValue.setText();

            // RESET ALL
            stopwatch.stop();
            clicked = false;
            countSuccess = 0; countFail = 0;
        }
    }

    protected void initVariables() {
        send = findViewById(R.id.send);
        packetLossValue = findViewById(R.id.packetLossValue);
        responseTimeValue = findViewById(R.id.responseTimeValue);
        cpuProcessingValue = findViewById(R.id.cpuProcessingValue);
        payloadSizeValue = findViewById(R.id.payloadSizeValue);
        responseSuccessValue = findViewById(R.id.responseSuccessValue);
        responseFailValue = findViewById(R.id.responseFailValue);
        status = findViewById(R.id.status);
    }
}
