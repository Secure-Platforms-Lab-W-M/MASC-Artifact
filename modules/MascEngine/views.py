from django.shortcuts import render

from modules.CipherManager.models import PropertiesList


# Create your views here.
def index(request):
    scopes = ['Similarity', 'Exhaustive']
    records = PropertiesList.objects.all().values()
    return render(request, "masc-engine/engine.html", {
        "scopes": scopes,
        "properties_file": records
    })


# Create your views here.
def history(request):
    custome_operator_headers = ["Uploaded File", "Selected Operator", "Status", "Actions"]
    return render(request, "masc-engine/history.html", {
        "custome_operator_headers": custome_operator_headers
    })
