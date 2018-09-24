客户端的实现：
<br>android客户端：采用整体mvc架构开发，ui风格上采用的是Material Design样式，界面之间切换由ViewPage+Fragment形成。app的总体分成了校车查询，校园电话，校园风光，校园生活等等模块，地图模块，采用了百度地图sdk进行开发，通过阅读开发文档，完成了用户定位，卫星地图，以及全景（街景）地图的功能，让想报考南昌大学的学生可以提前游览校园。为了提高应用的性能，避免出现ANR，所有的网络请求数据全部放在子线程中处理，采用的是Thread+Handle，AsyncTask等异步处理技术。同时数据条目请求和显示做了分页处理。新闻等有图片显示的采用的是UIL图片加载框架进行处理。消息推送则是在静态注册监听开机广播里启动Service服务采用ActiveMQ消息队列中间件mqtt订阅服务器主题。当有新消息时，通过通知栏Notification显示信息简略给用户，当点击通知栏信息条目后跳转到相关的页面进行展示详细信息。用户登录模块，用户名与密码采用了MD5+加盐操作进行加密，保证用户信息安全。数据传输采用长连接Socket通信，因为课本上正好讲到这个，所以就采用了它而没有采用短连接http。通信格式采用的是Json，json的生成与解析采用的是阿里巴巴的FastJson框架。
<br>服务器端的实现：采用JavaWeb开发技术+Tomcat实现。为了处理多用户情况下的数据业务请求响应的并发问题，引入了企业服务总线（这里选择的是ActiveMQ）。客户端与服务器的通信思路如下所示：
<br>客户端与服务器、服务器内部数据传输思路：
<br>用户端通过Socket通信把需求封装成Json格式传递给web服务器。
<br>Web服务后端的Socket监听服务接收到了用户端的请求后，将请求放入ActiveMQ的请求队列中。ActiveMQ消息队列中间件，支持异步处理，所以服务器可以继续监听其他用户端请求。而CORE业务监听服务监听到请求队列中有消息后，便从请求队列中取出消息，对消息进行拆包，根据需求进行相关的业务和逻辑处理，通过JDBC方式与MySQL进行数据CRUD操作。
<br>当Web服务器处理完成后，把数据库JDBC操作的结果在CORE业务监听服务封装成Json格式放入ActiveMQ的响应队列中，并且为消息加上用户id标记。Socket监听服务监听到响应队列中有消息后，根据请求的用户端id便从请求队列中取出带有对应标记id的消息，再通过Socket通信给用户端或者管理员端传输回去。当有新消息需要推送给多个用户端时，则在Web服务器与用户端之间数据传输采用mqtt协议，它支持一对多的发布/订阅模式。
界面效果如下：
<br>![Alt text](https://github.com/liangzhanncu/intelligenceNCU/blob/master/app/src/main/res/mipmap-mdpi/1.png)
<br>![Alt text](https://github.com/liangzhanncu/intelligenceNCU/blob/master/app/src/main/res/mipmap-mdpi/2.png)
<br>![Alt text](https://github.com/liangzhanncu/intelligenceNCU/blob/master/app/src/main/res/mipmap-mdpi/3.png)
<br>![Alt text](https://github.com/liangzhanncu/intelligenceNCU/blob/master/app/src/main/res/mipmap-mdpi/4.png)
<br>![Alt text](https://github.com/liangzhanncu/intelligenceNCU/blob/master/app/src/main/res/mipmap-mdpi/5.png)
<br>![Alt text](https://github.com/liangzhanncu/intelligenceNCU/blob/master/app/src/main/res/mipmap-mdpi/6.png)
<br>![Alt text](https://github.com/liangzhanncu/intelligenceNCU/blob/master/app/src/main/res/mipmap-mdpi/7.png)
<br>![Alt text](https://github.com/liangzhanncu/intelligenceNCU/blob/master/app/src/main/res/mipmap-mdpi/8.png)
