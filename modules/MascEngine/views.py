from django.shortcuts import render

# Create your views here.
def index(request):
    scopes = ['Similarity', 'Exhaustive']
    return render(request, "masc-engine/engine.html")
# Create your views here.
def history(request):
    custome_operator_headers = ["Uploaded File","Selected Operator", "Status", "Actions"]
    return render(request, "masc-engine/history.html",{
        "custome_operator_headers": custome_operator_headers
    })