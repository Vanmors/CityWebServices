#!/bin/bash

## Запуск PostgreSQL
#echo "Starting PostgreSQL..."
#/etc/init.d/postgresql start
#
#if [ ! -d "/var/lib/postgresql/12/main" ]; then
#  echo "Initializing PostgreSQL database..."
#  su postgres -c "initdb --locale en_US.UTF-8 -D /var/lib/postgresql/12/main"
#fi
#
#sed -i "s/#listen_addresses = 'localhost'/listen_addresses = '*'/" /etc/postgresql/12/main/postgresql.conf
#
## Ожидание, чтобы убедиться, что PostgreSQL работает
#sleep 5
#pg_isready -U postgres
#if [ $? -ne 0 ]; then
#  echo "PostgreSQL did not start correctly"
#  exit 1
#fi

# Запуск Payara
echo "Starting Payara..."

#/opt/payara/bin/asadmin start-domain &

/opt/payara/bin/asadmin start-domain

# Задаем пароль администратора Payara
echo "AS_ADMIN_PASSWORD=" > /tmp/password.txt
echo "AS_ADMIN_NEWPASSWORD=admin" >> /tmp/password.txt

# Меняем пароль администратора
/opt/payara/bin/asadmin --user admin --passwordfile /tmp/password.txt change-admin-password --domain_name domain1

# Перезапускаем домен, чтобы изменения вступили в силу
/opt/payara/bin/asadmin stop-domain
/opt/payara/bin/asadmin start-domain

# Теперь включаем secure-admin
echo "AS_ADMIN_PASSWORD=admin" > /tmp/password-enable.txt
/opt/payara/bin/asadmin --user admin --passwordfile /tmp/password-enable.txt enable-secure-admin

# Перезапускаем домен еще раз после включения secure-admin
/opt/payara/bin/asadmin stop-domain
/opt/payara/bin/asadmin start-domain

#/opt/payara/bin/asadmin start-domain

# Логирование процессов
echo "All services started. Logging..."
#tail -f /var/log/postgresql/postgresql-*.log /opt/payara/glassfish/domains/domain1/logs/*.log
#tail -f /var/log/postgresql/postgresql-*.log
tail -f /dev/null
