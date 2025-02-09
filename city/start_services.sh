#!/bin/bash

# Запуск PostgreSQL
echo "Starting PostgreSQL..."
/etc/init.d/postgresql start

if [ ! -d "/var/lib/postgresql/12/main" ]; then
  echo "Initializing PostgreSQL database..."
  su postgres -c "initdb --locale en_US.UTF-8 -D /var/lib/postgresql/12/main"
fi

sed -i "s/#listen_addresses = 'localhost'/listen_addresses = '*'/" /etc/postgresql/12/main/postgresql.conf

# Ожидание, чтобы убедиться, что PostgreSQL работает
sleep 5
pg_isready -U postgres
if [ $? -ne 0 ]; then
  echo "PostgreSQL did not start correctly"
  exit 1
fi

# Запуск Payara
echo "Starting Payara..."
/opt/payara/payara5/bin/asadmin start-domain &

# Логирование процессов
echo "All services started. Logging..."
tail -f /var/log/postgresql/postgresql-*.log /opt/payara/payara5/glassfish/domains/domain1/logs/server.log
