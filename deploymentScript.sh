echo "Please enter C for core and S for Script"

read scriptToDeploy

if [ "$scriptToDeploy" == "C" ]; then
  echo -e "You have chosen core bundle for deployment\nNow please enter the runMode as A for author and P for publish"
  read runMode
    read -e -p "Enter Host:" -i "localhost" host
    read -e -p "Enter port:" -i "4502" posport
    read -e -p "Enter user:" -i "admin" user
    read -s -e -p "Enter password[defaults to admin]:" -i "admin" password

MAVEN_FLAGS="-D aem.host=$host -D aem.port=$port -D sling.user=$user -D sling.password=$password -D vault.user=$user -D vault.password=$password"

  if [ "$runMode" == "A" ]; then
    mvn clean install -P autoInstallPackage $MAVEN_FLAGS
  elif [ "$runMode" == "P" ]; then
      mvn clean install -P autoInstallPackagePublish -D $MAVEN_FLAGS
      else
        echo "Wrong input entered"
  fi
elif [ "$scriptToDeploy" == "S" ]; then
  echo "You have chosen Scripts for deployment"
    read -e -p "Enter Host:" -i "localhost" host
    read -e -p "Enter port:" -i "4502" port
    read -e -p "Enter user:" -i "admin" user
    read -s -e -p "Enter password[defaults to admin]:" -i "admin" password

MAVEN_FLAGS="-D aem.host=$host -D aem.port=$port -D sling.user=$user -D sling.password=$password -D vault.user=$user -D vault.password=$password"

  cd supportUtils
  mvn clean install -P autoInstallScript -D aem.host=$host -D aem.port=$port -D sling.user=$user -D sling.password=$password -D vault.user=$user -D vault.password=$password
else
  echo "Wrong input entered"
fi

