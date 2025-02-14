Vagrant.configure("2") do |config|
      config.hostmanager.enabled = true
      config.hostmanager.manage_host = true
      ### jenkins VM ###
      config.vm.define "jenkins" do |jenkins|
        jenkins.vm.box = "ubuntu/jammy64"
        jenkins.vm.hostname = "jenkins"
      jenkins.vm.network "private_network", ip: "192.168.56.11"
    #  jenkins.vm.network "public_network"
      jenkins.vm.provider "virtualbox" do |vb|
         vb.gui = true
         vb.memory = 4096  # Sets RAM to 4GB
         vb.cpus = 2       # Sets number of CPU cores to 2
       end
      jenkins.vm.provision "shell", path: "jenkins.sh"
    end

    ### DB vm  ####
  config.vm.define "db01" do |db01|
    db01.vm.box = "eurolinux-vagrant/centos-stream-9"
    db01.vm.box_version = "9.0.43"
    db01.vm.hostname = "db01"
    db01.vm.network "private_network", ip: "192.168.56.15"
    db01.vm.provider "virtualbox" do |vb|
     vb.memory = "600"
   end
    db01.vm.provision "shell", path: "mysql.sh"  

  end
  
       ### tomcat vm ###
       config.vm.define "tomcat" do |tomcat|
        tomcat.vm.box = "gbailey/amzn2"
        tomcat.vm.box_version = "20241208.0.0"
        tomcat.vm.hostname = "tomcat"
        tomcat.vm.network "private_network", ip: "192.168.56.12"
        tomcat.vm.provision "shell", path: "tomcat.sh"
        tomcat.vm.provider "virtualbox" do |vb|
         vb.memory = "800"
       end
    end

end
