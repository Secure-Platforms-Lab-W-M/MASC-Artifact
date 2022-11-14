from django.shortcuts import render

from modules.plugins.models import PluginsList


# Create your views here.
def index(request):
    # operator
    # make any.properties file
    files_header = ["Name", "File Name", "Path", "Actions"]
    records = PluginsList.objects.all().values()
    return render(request, "plugins/index.html",{
        "headers": files_header,
        "records": records
    })


def handle_uploaded_file(f):
    with open('./modules/static/plugins/' + f.name, 'wb') as destination:
        for chunk in f.chunks():
            destination.write(chunk)
    return destination.name


def uploadPlugins(request):
    if request.method == 'POST':
        name = request.POST['name']
        filename = request.FILES['file'].name
        path = handle_uploaded_file(request.FILES['file'])  # path from masc core shall be added
        data = PluginsList(name=name, filename=filename, path=path);
        data.save()
        return render(request,'plugins/thanks.html')
    # list_of_operators = ["StringOperator","ByteOperator", "InterprocOperator", "Flexible", "IntOperator"]
    return render(request, "plugins/upload.html")
