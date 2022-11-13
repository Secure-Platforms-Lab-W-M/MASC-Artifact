from django.shortcuts import render

# Create your views here.
def index(request):
    # operator
    # make any.properties file
    return render(request, "plugins/index.html")