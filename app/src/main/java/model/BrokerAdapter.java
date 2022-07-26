package model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteproperties.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BrokerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Broker> brokerList;
    Broker broker;

    public BrokerAdapter(Context context, ArrayList<Broker> brokerList) {
        this.context = context;
        this.brokerList = brokerList;
    }

    @Override
    public int getCount() {
        return brokerList.size();
    }

    @Override
    public Object getItem(int position) {
        return brokerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View oneItem = null;
        TextView tvName,tvPhno,tvEmailId,tvExp;
        int pos = position;

        LayoutInflater inflater = LayoutInflater.from(context);
        oneItem = inflater.inflate(R.layout.one_broker,parent,false);

        tvName = oneItem.findViewById(R.id.tvName);
        tvPhno = oneItem.findViewById(R.id.tvPhNo);
        tvEmailId = oneItem.findViewById(R.id.tvEmailId);
        tvExp = oneItem.findViewById(R.id.tvExp);

        broker = (Broker)getItem(position);

        tvName.setText(broker.getFirstName()+" "+ broker.getLastName());
        tvPhno.setText("Ph : "+broker.getContactNo());
        tvEmailId.setText("Email Id : "+broker.getEmailId());
        tvExp.setText("Experience : "+broker.getExperience());


        return oneItem;
    }
}
