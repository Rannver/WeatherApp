# WeatherApp

###实现功能：
    1、国内各城市查询
    2、城市的添加和删除
    3、所选城市天气详情，包括今日天气、过去四天以及未来四天
    4、刷新功能
    5、在网络未连接时读取数据库存储的天气信息
    6、默认城市为所选的第一个城市，无默认城市时自动添加北京
    
###apk地址：
    http://pan.baidu.com/s/1c1VPJHm
    
###主要功能的代码实现：
    1、ApiStore的SDK调用Api，实现城市查询、今日天气、过去和未来的天气数据源获取
    2、RecyclerView实现未来四天和过去四天的天气情况的实现
    3、SQLite实现本地缓存，通过数据库本地读取天气信息，联网时自动更新原有天气信息
    4、通过检查数据库中所存储的城市，判断是否已添加此城市，添加则不存入数据库
    5、百分比布局库中PercentRelativeLayout实现屏幕自适应
      （2.2 preview6中失效原因不明，改用RelativeLayout下LinearLayout混合使用实现屏幕自适应）
      
###其他的记录：
    1、Android反编译（然而事实证明需要获取别人家apk图标的时候可以直接解压apk）
    2、RecyclerView的item不能使用PercentRelativeLayout实现屏幕自适应，通过Adpter动态匹配找不到它的父布局，显示不出内容
  
