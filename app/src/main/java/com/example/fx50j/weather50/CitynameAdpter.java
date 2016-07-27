package com.example.fx50j.weather50;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by FX50J on 2016/7/14.
 */
public class CitynameAdpter extends BaseAdapter{


    private List<CitynameBean> citynameList;
    private LayoutInflater inflater;

    public CitynameAdpter(Context context,List<CitynameBean> citylist){

        citynameList =citylist;
        inflater = LayoutInflater.from(context);

    }


    @Override
    public int getCount() {
        return citynameList.size();
    }

    @Override
    public Object getItem(int position) {
        return citynameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder=null;

        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.cityname_item,null);
            viewHolder.tv_city = (TextView) convertView.findViewById(R.id.tv_city);
            viewHolder.tv_province= (TextView) convertView.findViewById(R.id.tv_province);
            viewHolder.tv_country = (TextView) convertView.findViewById(R.id.tv_district);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
           // Log.d("else","执行了else的代码");
        }
        viewHolder.tv_province.setText(citynameList.get(position).GetProvince());
        viewHolder.tv_city.setText(citynameList.get(position).GetCity());
        viewHolder.tv_country.setText(citynameList.get(position).GetCountry());

        return convertView;
    }

    static class ViewHolder{
        TextView tv_province,tv_city,tv_country;
    }
}

