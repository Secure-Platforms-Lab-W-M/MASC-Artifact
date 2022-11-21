#!/bin/bash
source venv/bin/activate
python manage.py makemigrations
python manage.py migrate
python manage.py runserver
