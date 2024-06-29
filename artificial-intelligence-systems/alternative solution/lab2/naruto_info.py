from flask import Flask, jsonify, request
from deeppavlov import build_model
from deeppavlov.core.common.file import read_json

app = Flask(__name__)

context = open("naruto-info.txt").read()
model_config_bert = read_json("squad_ru_bert_infer.json")
model_bert = build_model(model_config_bert, download=True)

@app.route('/', methods=['POST'])
def naruto():
    data = request.get_json()
    question = data['data']
    answer = model_bert([context], [question])
    if answer[0][0].strip() and answer[2][0] == 1:
        return jsonify({"data": answer})
    elif answer[2][0] < 1 and answer[0][0].strip():
        return jsonify({"data": "Я не могу дать точный ответ на вопрос"})
    else:
        return jsonify({"data": "Вопрос не по тексту"})

if __name__ == '__main__':
    app.run(port=5002)
