import telebot
import sys
from deeppavlov import build_model, configs
from deeppavlov.core.common.file import read_json

model_config = read_json("squad_ru_bert_infer.json")
model = build_model(model_config, download=True)

tokenFile = open("token.txt")
token = tokenFile.readline()

contextFile = open("text-about-bashkortostan.txt")
context = contextFile.read()

bot = telebot.TeleBot(token)

@bot.message_handler(commands=["start"])
def send_welcome(message):
    bot.reply_to(message, "Сәләм. Башҡортостан буйынса һорауҙар бирә алаһың.")

@bot.message_handler(func=lambda message: True)
def echo_all(message):
	messageText = message.text
	answer = model([context], [messageText])
	if answer[0][0].strip():
		bot.reply_to(message, answer)
	else:
		bot.reply_to(message, "Мин һине аңламаным")

bot.infinity_polling()
