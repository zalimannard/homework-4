import multiprocessing
from multiprocessing import Process
from enum import Enum
import telebot
import os
import sys
import json
import requests
from deeppavlov import build_model, train_model, configs
from deeppavlov.core.common.file import read_json

class Modes(Enum):
    INITIAL = 1
    TRANSLATE = 2
    QUESTINGANSWERING = 3

intent_catcher_model_config = read_json("intent_catcher.json")

botTokenFile = open("botToken.txt")
botToken = botTokenFile.readline()

iamTokenFile = open("iamToken.txt")
iamToken = iamTokenFile.readline()

contextFile = open("text-about-bashkortostan.txt")
context = contextFile.read()

welcomeText = "Добро пожаловать в путеводитель. Вы можете:\n- Задавать вопросы про Башкортостан\n- Переводить слова на башкирский\n- Получить главные ссылки Республики"

bot = telebot.TeleBot(botToken)

@bot.message_handler(commands=["start"])
def send_welcome(message):
    bot.reply_to(message, welcomeText)

def translate(phrase):
    folder_id = 'b1gg8r44dbvn6d0h59cv'
    target_language = 'ba'
    texts = [phrase]

    body = {
        "targetLanguageCode": target_language,
        "texts": texts,
        "folderId": folder_id,
    }

    headers = {
        "Content-Type": "application/json",
        "Authorization": "Bearer {0}".format(iamToken)
    }

    response = requests.post('https://translate.api.cloud.yandex.net/translate/v2/translate',
        json = body,
        headers = headers
    )

    return response.text

MODE = Modes.INITIAL

@bot.message_handler(content_types=['text'])
def messageHandler(message):
    global MODE
    queue.put(message.text)
    intent_result = queue.get()

    if intent_result[0] == 'initial':
        bot.send_message(message.chat.id, welcomeText)
        MODE = Modes.INITIAL

    elif (MODE == Modes.TRANSLATE):
        answer = translate(message.text)
        answer_json = json.loads(answer)
        answer_phrase = answer_json["translations"][0]["text"]
        print(answer_phrase)
        bot.reply_to(message, answer_phrase)

    elif (MODE == Modes.QUESTINGANSWERING):
        messageText = message.text
        os.system("python costilMain.py \"" + str(message.chat.id) + "\" " + "\"" + messageText + "\"")

    elif intent_result[0] == 'translate':
        bot.send_message(message.chat.id, "Жду фразу для перевода на башкирский")
        MODE = Modes.TRANSLATE

    elif intent_result[0] == 'cqa':
        bot.send_message(message.chat.id, "Что бы вы хотели узнать про Башкортостан?")
        MODE = Modes.QUESTINGANSWERING

    elif intent_result[0] == 'urls':
        bot.send_message(message.chat.id, "Интересные ссылки Республики:")
        bot.send_message(message.chat.id, open("urls.txt", encoding="utf-8").read())

    else:
        bot.reply_to(message, "Мин һине аңламаным")

    print(MODE)


def work_with_intent_catcher_model(q):
    # intent_catcher_model = build_model(intent_catcher_model_config)
    intent_catcher_model = train_model(intent_catcher_model_config)
    q.put(1)
    while True:
        q.put(intent_catcher_model([q.get()]))

if __name__ == '__main__':
    queue = multiprocessing.Queue()
    child_process = Process(target=work_with_intent_catcher_model, args=(queue,))
    child_process.start()
    queue.get()
    print("Начало работы")
    bot.infinity_polling()

