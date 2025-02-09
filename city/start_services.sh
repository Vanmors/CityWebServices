#!/bin/bash

# Стартуем PostgreSQL
service postgresql start

# Стартуем Payara сервер
/opt/payara/payara5/bin/asadmin start-domain &

# Делаем так, чтобы контейнер не завершался, и сервисы продолжали работать
tail -f /dev/null
