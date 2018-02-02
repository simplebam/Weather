## SeeWeather
就看天气——是一款遵循 Material Design 风格的只看天气的APP。 <br/>
卡片展现（当前天气情况，未来几小时天气情况，生活建议，一周七天概况） <br/>
缓存数据，减少网络请求，保证离线查看 <br/>
内置两套图标（设置里更改）

原作者Github项目:[xcc3641/SeeWeather](https://github.com/xcc3641/SeeWeather) <br/>
<a href="../art/seeweather_home.jpg"><img src="../art/seeweather_home.jpg" width="20%"/></a><img height="0" width="8px"/><a href="../art/seeweather_setting.jpg"><img src="../art/seeweather_setting.jpg" width="20%" /></a><img height="0" width="8px"/>


### 项目中用到的知识
* 开发规范:[Android 开发规范（完结版） - 简书](https://www.jianshu.com/p/45c1675bec69)
* Gradle
   * [Android Gradle权威指南](http://www.java1234.com/a/javabook/andriod/2018/0117/10280.html)
     网上还出了《实战Gradle》以及《Gradle for Android 中文版》等Gradle书籍,其
     实Gradle书籍一般都会存在知识点不齐全问题,所以我建议选择这本入门,之后结合百
     度或者博客等获取到你需要的Gradle知识即可
   * [新一代构建工具gradle-慕课网](https://www.imooc.com/learn/833)
* [TinyPNG – Compress PNG](https://tinypng.com/),目前我遇到最好用的图像压缩
  在线网站,一般图片都是经过压缩才会用的,网上很多压缩网站都是水印免费,无水印开会员
  ,真的恶心!!!


### 项目中的用到的框架
* OrmLite-Android高性能数据库框架
  * [Android 快速开发系列之数据库篇（LiteOrm） -简书](https://www.jianshu.com/p/0d72226ef434)
  * [litesuits/android-lite-orm](https://github.com/litesuits/android-lite-orm)
  * 由于OrmLite已经很久没有更新以及没有使用Gradle配置方式,这里我个人建议大家可
    以尝试使用郭霖先生的LitePal(原理上跟OrmLite一样的),升级数据库可以不清数据:
    [Android数据库高手秘籍(一)——SQLite命令 - 郭霖的专栏 - CSDN博客](http://blog.csdn.net/guolin_blog/article/details/38461239)
    以及[LitePal 1.6.0版本来袭，数据加解密功能保障你的应用数据安全](http://mp.weixin.qq.com/s/TSp36cnKLxUmAHjT86UCrQ)
* bugtags-移动时代首选 Bug 管理系统:[Bugtags 使用说明 - CSDN博客](http://blog.csdn.net/ObjectivePLA/article/details/51037804)
* Watcher-Help to watch the fps and used memory of your app(检测App的已用
  内存以及帧/秒)
  * [xcc3641/Watcher](https://github.com/xcc3641/Watcher)
* stetho-Android调试工具。它可以通过chrome的开发者工具来辅助安卓开发。
  * [stetho使用介绍 - 简书](https://www.jianshu.com/p/c03a8959d1a5)
* blockcanary-自动检测性能卡顿的工具
  * [找出造成Android App界面卡顿的原因- BlockCanary - 简书](https://www.jianshu.com/p/5d9eca9c343a)
  * [BlockCanary分析android卡顿 - bazhongren的博客 - CSDN博客](http://blog.csdn.net/bazhongren/article/details/51125113)
    这里我需要说明一点:就是这两篇介绍的继承类方法在新版BlockCanary里面的方法名
    字已经变更了,但其他是没什么变化的
* RxJava
   * 目前最好的RxJava入门文章,没有之一:[给初学者的RxJava2.0教程(一) - 简书 ](https://www.jianshu.com/p/464fa025229e)
     以及对应的项目教程源码:[ssseasonnn/RxJava2Demo](https://github.com/ssseasonnn/RxJava2Demo)
   * [这可能是最好的RxJava 2.x 教程（完结版）- 简书](https://www.jianshu.com/p/0cd258eecf60)
   * 其他RxJava文章推荐:[RxJava2 学习资料推荐](http://mp.weixin.qq.com/s/UAEgdC2EtqSpEqvog0aoZQ)
   * [RxJava之过滤操作符 - 行云间 - CSDN博客](http://blog.csdn.net/io_field/article/details/51378909)
   * [RxJava2使用过程中遇到的坑 - CSDN博客](http://blog.csdn.net/sr_code_plus/article/details/77189478)
* RxPermissions
   * [RxPermissions获取运行时权限 - 简书](https://www.jianshu.com/p/314e9e27592f)
   * 当用户选择了“不再提示+拒绝”之后的处理,只能移步到系统设置去打开权限了
   * 在onResume()中处理监听到设置了禁止后不再询问
* Retrofit
   * [你真的会用Retrofit2吗?Retrofit2完全教程 - 简书](https://www.jianshu.com/p/308f3c54abdd)
* rxlifecycle
   * [RxAndroid之Rxlifecycle使用 - 享受技术带来的快乐 - CSDN博客](http://blog.csdn.net/jdsjlzx/article/details/51527542)
   * [Android 性能优化之利用 Rxlifecycle 解决 RxJava 内存泄漏 ](https://juejin.im/entry/58290ea2570c35005878ce8f)

PS:debug情况下使用stetho以及Watcher,release版本使用bugstag(原项目使用bughd)


### 项目开发遇到的问题
* 其实当AS升级到3.0之后,若项目之前用了类似retrolambda(原项目用了)或者Jack这种
  旧方案的话，会出现以下提示告诉你移除相关的代码：
  > Warning:One of the plugins you are using supports Java 8 language features. To try the support built into the Android plugin, remove the following from your build.gradle: apply plugin: ‘me.tatarka.retrolambda’ To learn more, go to https://d.android.com/r/tools/java-8-support-message.html

  解决办法:[Android Studio 3.0及Gradle Plugin 3.0升级注意事项](https://codezjx.github.io/2017/11/23/gradle-plugin-3-0-0-migration/)

* 之前的Gradle配置[Android 开发之版本统一规范](https://blankj.com/2016/09/21/android-keep-version-unity/)
  或者[android studio编程时出现的错误：Cannot get property 'XXXX' on extra
   properties extension as it does not exist - CSDN博客](http://blog.csdn.net/qq_22078107/article/details/53349836)
  ,都是直接在build.gradle里面配置的,第一次见另外起一个config.gradle来配置相关
  信息的,但配置了大概之后sync一下就出现下面的bug:
  > Error:(4, 0) Cannot get property 'android' on extra properties extension as it does not exist
    <a href="openFile:E:\acode\Android_Studio\Weather\seeweather\build.gradle">Open File</a>

  * 解决办法:在Project的build.gradle首部加上 apply from: 'config.gradle'
* 原项目开发使用的是[BugHD](http://bughd.com/doc/ios-version-update),但很
  遗憾就是它已经停止免费服务了,在这里我打算替换成bugtags


### 项目中数据提供
* 天气数据:[和风天气 | 好用的气象数据服务 天气数据分析 天气商业化解决方案](https://www.heweather.com/)
* App内测:[fir.im - 免费应用内测托管平台|iOS/Android应用内测分发](https://fir.im/)

 
## 推荐阅读-推荐的不仅仅是技术
* [2017 Android 最全面试总结 - 这些面试题你一定需要 - Android - 掘金](https://juejin.im/entry/58b7a8f48d6d810065274ef1)
  