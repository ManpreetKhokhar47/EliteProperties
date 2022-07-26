package model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eliteproperties.PropertyList;
import com.example.eliteproperties.R;
import com.example.eliteproperties.propertyDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PropertyAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Property> propertyList;
    Property property;

    public PropertyAdapter(Context context, ArrayList<Property> propList) {
        this.context = context;
        this.propertyList = propList;
    }

    @Override
    public int getCount() {
        return propertyList.size();
    }

    @Override
    public Object getItem(int position) {
        return propertyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View oneItem = null;
        ImageView imgProp;
        TextView tvPrice,tvAddress,tvBeds,tvBaths,tvParking;
        Button btnAval;
        int pos = position;

        LayoutInflater inflater = LayoutInflater.from(context);
        oneItem = inflater.inflate(R.layout.one_property, parent,false);

        imgProp = oneItem.findViewById(R.id.imgPropA);
        tvPrice = oneItem.findViewById(R.id.tvPriceA);
        tvAddress = oneItem.findViewById(R.id.tvAddressA);
        tvBeds = oneItem.findViewById(R.id.tvBedroomsA);
        tvBaths = oneItem.findViewById(R.id.tvBathroomsA);
        tvParking = oneItem.findViewById(R.id.tvparkingA);
        btnAval = oneItem.findViewById(R.id.btnAvailabilityA);

        property = (Property) getItem(position);
        String photoStr = property.getSrcImg();

        Picasso.get().load(photoStr).into(imgProp);
        tvPrice.setText("$ "+String.valueOf(property.getPrice()));
        tvAddress.setText(property.getAddress());
        tvBeds.setText(property.getBedroomsCount());
        tvBaths.setText(property.getBathroomCount());
        tvParking.setText(property.getParkingSpaces());
        btnAval.setText("Available Now !");

        return oneItem;
    }
}
