package com.example.fx50j.weather50;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * RecycleView的适配器
 */
public class FutureWeatherAdpter extends RecyclerView.Adapter<FutureWeatherAdpter.ViewHolder> {

    private List<FutureWeatherBean> futureWeatherList;

    //调用构造函数传递数据（这点竟然想这么久mdzz
    public FutureWeatherAdpter(List<FutureWeatherBean> futureweatherList){

        futureWeatherList = futureweatherList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.future_item,null);
        ViewHolder viewHolder =new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tv_date.setText( futureWeatherList.get(position).GetDate());
        holder.tv_temp.setText(futureWeatherList.get(position).GetHightemp()+"~"+futureWeatherList.get(position).GetLowtemp());
        holder.tv_type.setText(futureWeatherList.get(position).GetType() + "  " + futureWeatherList.get(position).GetWind());
        holder.tv_week.setText(futureWeatherList.get(position).GetWeek());

        String str =futureWeatherList.get(position).GetType();

        switch (str){
            case "晴":
                holder.iv_weather.setImageResource(R.drawable.w00);
                break;
            case "多云":
                holder.iv_weather.setImageResource(R.drawable.w01);
                break;
            case "阴":
                holder.iv_weather.setImageResource(R.drawable.w02);
                break;
            case "阵雨":
                holder.iv_weather.setImageResource(R.drawable.w11);
                break;
            case "雷阵雨":
                holder.iv_weather.setImageResource(R.drawable.w11);
                break;
            case "雷阵雨伴有冰雹":
                holder.iv_weather.setImageResource(R.drawable.w11);
                break;
            case "雨夹雪":
                holder.iv_weather.setImageResource(R.drawable.w07);
                break;
            case "小雨":
                holder.iv_weather.setImageResource(R.drawable.w03);
                break;
            case "中雨":
                holder.iv_weather.setImageResource(R.drawable.w06);
                break;
            case "大雨":
                holder.iv_weather.setImageResource(R.drawable.w05);
                break;
            case "暴雨":
                holder.iv_weather.setImageResource(R.drawable.w05);
                break;
            case "大暴雨":
                holder.iv_weather.setImageResource(R.drawable.w05);
                break;
            case "特大暴雨":
                holder.iv_weather.setImageResource(R.drawable.w05);
                break;
            case "阵雪":
                holder.iv_weather.setImageResource(R.drawable.w06);
                break;
            case "小雪":
                holder.iv_weather.setImageResource(R.drawable.w06);
                break;
            case "中雪":
                holder.iv_weather.setImageResource(R.drawable.w06);
                break;
            case "大雪":
                holder.iv_weather.setImageResource(R.drawable.w06);
                break;
            case "暴雪":
                holder.iv_weather.setImageResource(R.drawable.w10);
                break;
            case "雾":
                holder.iv_weather.setImageResource(R.drawable.w02);
                break;
            case "冻雨":
                holder.iv_weather.setImageResource(R.drawable.w04);
                break;
            case "沙尘暴":
                holder.iv_weather.setImageResource(R.drawable.w02);
                break;
            case "小到中雨":
                holder.iv_weather.setImageResource(R.drawable.w06);
                break;
            case "中到大雨":
                holder.iv_weather.setImageResource(R.drawable.w06);
                break;
            case "大到暴雨":
                holder.iv_weather.setImageResource(R.drawable.w05);
                break;
            case "暴雨到大暴雨":
                holder.iv_weather.setImageResource(R.drawable.w11);
                break;
            case "大暴雨到特大暴雨":
                holder.iv_weather.setImageResource(R.drawable.w11);
                break;
            case "小到中雪":
                holder.iv_weather.setImageResource(R.drawable.w06);
                break;
            case "中到大雪":
                holder.iv_weather.setImageResource(R.drawable.w06);
                break;
            case "大到暴雪":
                holder.iv_weather.setImageResource(R.drawable.w10);
                break;
            case "浮尘":
                holder.iv_weather.setImageResource(R.drawable.w02);
                break;
            case "扬沙":
                holder.iv_weather.setImageResource(R.drawable.w02);
                break;
            case "强沙尘暴":
                holder.iv_weather.setImageResource(R.drawable.w02);
                break;
            case "霾":
                holder.iv_weather.setImageResource(R.drawable.w02);
                break;
            default:
                holder.iv_weather.setImageResource(R.drawable.unknow);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return futureWeatherList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_weather;
        public TextView  tv_temp;
        public TextView  tv_type;
        public TextView  tv_date;
        public TextView  tv_week;

        public ViewHolder(View itemView) {
            super(itemView);

            iv_weather = (ImageView) itemView.findViewById(R.id.iv_weather);
            tv_temp = (TextView) itemView.findViewById(R.id.tv_temp);
            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_week = (TextView)itemView.findViewById(R.id.tv_week);
        }
    }
}
