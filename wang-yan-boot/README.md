## docker

### Docker
- Centos7+yum来安装Docker 
``` shell
yum install docker
```
- 启动 docker 服务
``` docker
service docker start
chkconfig docker on
```
- 旧式的 sysv 语法，如采用CentOS 7中支持的新式 systemd语法，如下： 
``` shell
systemctl start docker.service
systemctl enable docker.service
```
- 测试 
``` docker
docker version
```
- 镜像加速 
``` 
vi /etc/docker/daemon.json 
#添加后 
{ 
    "registry-mirrors":["https://registry.docker-cn.com"], 
    "live-restore": true 
} 
```

### Docker Compose
    配置文件定义一个多容器的应用
- 安装 
    - 方法一
        ```
        #下载
        sudo curl -L https://github.com/docker/compose/releases/download/1.20.0/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
        #安装
        chmod +x /usr/local/bin/docker-compose
        #查看版本
        docker-compose version
        ```
  -   方法二 
        ```
        #安装pip
        yum -y install epel-release
        yum -y install python-pip
        #确认版本
        pip --version
        #更新pip
        pip install --upgrade pip
        #安装docker-compose
        pip install docker-compose 
        #查看版本
        docker-compose version
        ```
- 测试 
```
docker-compose version
```
- 运行 
```
docker-copose up -d 
```
- 移除
```
docker compose down
```
- 彻底删除（掛載）
```
docker-compose down --volumes
```

**docker-compose.yaml：docker-compose 的核心文件，描述如何构建整个服务**
- version: '3'： 表示使用第三代语法来构建 docker-compose.yaml 文件。
- services: 用来表示 compose 需要启动的服务，我们可以看出此文件中有三个服务分别为：nginx、mysql、app。
- container_name: 容器名称
- environment: 此节点下的信息会当作环境变量传入容器，此示例中 mysql 服务配置了数据库、密码和权限信息。
- ports: 表示对外开放的端口
- restart: always 表示如果服务启动不成功会一直尝试。
- volumes: 加载本地目录下的配置文件到容器目标地址下
- depends_on：可以配置依赖服务，表示需要先启动 depends_on 下面的服务后，再启动本服务。
- command: mvn clean spring-boot:run -Dspring-boot.run.profiles=docker: 表示以这个命令来启动项目，-Dspring-boot.run.profiles=docker表示使用 application-docker.properties文件配置信息进行启动。

**project-tools/nginx/conf.d/app.conf**
- 将80端口的请求转发到服务 app 的8080端口。其中proxy_pass http://app:8080这块的配置信息需要解释一下，这里使用是app而不是localhost，是因为他们没有在一个容器中，在一组 compose 的服务通讯需要使用 services 的名称进行访问。
 
**Dockerfile**
- 依赖于基础镜像maven3.5和jdk 1.8。因为在docker-compose.yaml文件设置了项目启动命令，这里不需要再添加启动命令。