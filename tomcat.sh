#!/bin/bash

# Install Java (explicitly set JAVA_HOME)
amazon-linux-extras install -y java-openjdk11
export JAVA_HOME=$(dirname $(dirname $(readlink -f $(which java))))

# Download correct Tomcat version (9.0.85 instead of 9.0.99)
cd /opt/
wget https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.85/bin/apache-tomcat-9.0.85.tar.gz
tar -xzvf apache-tomcat-9.0.85.tar.gz
mv apache-tomcat-9.0.85 tomcat

# Create dedicated user and set permissions
useradd -r -m -U -d /opt/tomcat -s /bin/false tomcat
chown -R tomcat: /opt/tomcat
chmod +x /opt/tomcat/bin/*.sh

# Update context.xml files
sed -i 's/<Valve className="org.apache.catalina.valves.RemoteAddrValve"/<!-- <Valve className="org.apache.catalina.valves.RemoteAddrValve"/' /opt/tomcat/webapps/manager/META-INF/context.xml
sed -i 's/allow="127\.\\d+\.\\d+\.\\d+|\:\:1|0:0:0:0:0:0:0:1" \/>/allow="127\.\\d+\.\\d+\.\\d+|\:\:1|0:0:0:0:0:0:0:1" \/> -->/' /opt/tomcat/webapps/manager/META-INF/context.xml

sed -i 's/<Valve className="org.apache.catalina.valves.RemoteAddrValve"/<!-- <Valve className="org.apache.catalina.valves.RemoteAddrValve"/' /opt/tomcat/webapps/host-manager/META-INF/context.xml
sed -i 's/allow="127\.\\d+\.\\d+\.\\d+|\:\:1|0:0:0:0:0:0:0:1" \/>/allow="127\.\\d+\.\\d+\.\\d+|\:\:1|0:0:0:0:0:0:0:1" \/> -->/' /opt/tomcat/webapps/host-manager/META-INF/context.xml

# Configure users with secure passwords (consider using vault for production)
cat <<EOT >> /opt/tomcat/conf/tomcat-users.xml
<role rolename="manager-gui"/>
<role rolename="manager-script"/>
<role rolename="manager-jmx"/>
<role rolename="manager-status"/>
<user username="admin" password="$(openssl rand -base64 12)" roles="manager-gui,manager-script,manager-jmx,manager-status"/>
<user username="deployer" password="$(openssl rand -base64 12)" roles="manager-script"/>
EOT

# Create systemd service
cat <<EOT > /etc/systemd/system/tomcat.service
[Unit]
Description=Apache Tomcat Web Application Container
After=network.target

[Service]
Type=forking
User=tomcat
Group=tomcat
Environment=JAVA_HOME=$JAVA_HOME
Environment=CATALINA_PID=/opt/tomcat/temp/tomcat.pid
Environment=CATALINA_HOME=/opt/tomcat
Environment=CATALINA_BASE=/opt/tomcat

ExecStart=/opt/tomcat/bin/startup.sh
ExecStop=/opt/tomcat/bin/shutdown.sh

[Install]
WantedBy=multi-user.target
EOT

# Enable firewall (if enabled)
if command -v firewall-cmd &>/dev/null; then
  firewall-cmd --permanent --add-port=8080/tcp
  firewall-cmd --reload
fi

# Start and enable service
systemctl daemon-reload
systemctl start tomcat
systemctl enable tomcat

# Create symlinks
ln -s /opt/tomcat/bin/startup.sh /usr/local/bin/tomcatup
ln -s /opt/tomcat/bin/shutdown.sh /usr/local/bin/tomcatdown