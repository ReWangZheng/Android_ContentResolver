# 访问其他程序中的数据
&emsp;&emsp;内容提供器一般有两种用法，一种是使用现有的内容提供器来读取和操作相应程序中的数据，另外一种就是创建自己的内容提供器。如果一个应用程序通过内容提供器对其数据提供了外部访问接口，那么其他任何应用程序都可以对这部分数据进行访问。Android系统自带的电话拨，短信、媒体库等程序都提供了类似的访问接口。  
#### ContentResolver用法
&emsp;&emsp;对于每一个应用程序来说，如果想要访问内容提供器中共享的数据，就一定要借助`ContentResolver`类，我们可以通过Context类中的`getContentResolver()`方法来获取这个类的实例。    
&emsp;&emsp;*ContentResolver*提供了一系列的CRUD操作，与SQLiteDatabase中的几乎一模一样，不过第一个参数由表名变成了*URI*参数代替，通过这个*URI*来找到相对于的表。
>URI的组成：authority和path     
authority:一般都是程序名或者包名     
path:一般都是该程序中的某个表名     
>>格式:content://authority/path   
例子:content://com.exampe.wz/table1

得到URI对象的方法   
        
        Uri uri=Uri.parse("content://com.exampe.wz/table1");
        
由了这个URI对象，我们就可以进行CRUD的操作啦