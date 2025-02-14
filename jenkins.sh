#!/bin/bash

# Update system and install dependencies
apt-get update -y
apt-get upgrade -y

# Install required packages
apt-get install -y curl gnupg2

# Install Java (Jenkins LTS requires Java 11/17)
apt-get install -y openjdk-17-jdk

# Add Jenkins repository
curl -fsSL https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key | tee \
  /usr/share/keyrings/jenkins-keyring.asc > /dev/null
echo "deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc]" \
  https://pkg.jenkins.io/debian-stable binary/ | tee \
  /etc/apt/sources.list.d/jenkins.list > /dev/null

# Install Jenkins and Git
apt-get update -y
apt-get install -y jenkins git

# Install Maven
MAVEN_VERSION="3.9.9"
wget https://dlcdn.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz
mkdir -p /home/vagrant/maven
tar xzf apache-maven-${MAVEN_VERSION}-bin.tar.gz -C /home/vagrant/maven --strip-components=1
rm apache-maven-${MAVEN_VERSION}-bin.tar.gz
chown -R vagrant:vagrant /home/vagrant/maven

sudo apt install maven -y
# Configure environment variables
echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> /home/vagrant/.profile
echo 'export PATH=$PATH:$JAVA_HOME/bin' >> /home/vagrant/.profile
echo '' >> /home/vagrant/.profile
echo '# Maven Environment Variables' >> /home/vagrant/.profile
echo 'export M2_HOME=/home/vagrant/maven' >> /home/vagrant/.profile
echo 'export PATH=$PATH:$M2_HOME/bin' >> /home/vagrant/.profile

# Start and enable Jenkins service
systemctl start jenkins
systemctl enable jenkins

# Open firewall (if enabled)
ufw allow 8080
ufw allow OpenSSH
ufw --force enable

# Display initial admin password
echo "Jenkins initial admin password:"
cat /var/lib/jenkins/secrets/initialAdminPassword
