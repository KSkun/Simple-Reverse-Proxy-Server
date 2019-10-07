# Simple-Reverse-Proxy-Server
一个简单的反向代理服务器项目。KSkun的冰岩作坊实习项目。

## 功能

- **读取nginx格式（简化）的服务器配置文件。** 该文件应当放置于工作目录下，并命名为`rps.conf`。如果该文件不存在，程序将复制一份默认配置文件至工作目录下。默认配置文件的内容可以参考/src/main/resources/rps.conf。
- **记录访问日志和错误日志。** 将客户端的每一次请求的信息记录为日志文件。如果该日志文件已经存在，则将其重命名为`<filename>.n`，其中`n`是连续的正整数，以此备份之前的日志文件。
- **进行反向代理（HTTP GET请求与回复的转发）。** 转发客户端的GET请求至目标服务器，修改指定HTTP请求头字段，转发服务器的回复至客户端，以及简单的错误处理。该功能仅处理了部分HTTP异常。
- **进行静态代理（将本地文件传输至客户端）。** 将请求的本地文件数据发送至客户端，数据使用GZIP算法压缩。该功能忽略URL中的参数，且忽略客户端请求头中的`Accept`字段。
- **进行负载均衡（实现nginx中upstream功能）。** 将连接按一定算法分配到不同的后端服务器进行处理，实现负载均衡功能。算法支持轮询、加权轮询和ip_hash三种。

## 使用

如果本机配置了gradle，可以使用`gradle run`直接编译运行项目。工作目录被重定向至项目根目录下的`testdir`文件夹下，请在该文件夹下放置配置文件、查看日志文件等。

如果使用的是本项目的jar文件，其默认工作目录为其所在的目录，请将配置文件、root文件夹等与其置于同一目录下，并直接使用`java -jar xxx.jar`运行其即可。

