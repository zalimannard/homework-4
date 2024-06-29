from deeppavlov import build_model
from deeppavlov.core.common.file import read_json
import requests
import json
from flask import Flask, jsonify, request
from deeppavlov import build_model
from deeppavlov.core.common.file import read_json

app = Flask(__name__)


model_config_roberta = read_json("few_shot_roberta.json")
model_roberta = build_model(model_config_roberta, download=True)

dataset = []
with open('train.json', 'r') as file:
    dataset = json.load(file)


@app.route('/', methods=['POST'])
def int():
    data = request.get_json()['data']
    intent = model_roberta([data], dataset)[0]
    return jsonify({"data": intent})

if __name__ == '__main__':
    app.run(port=5000)


