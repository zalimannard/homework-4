import telebot
import sys
from deeppavlov import build_model, configs
from deeppavlov.core.common.file import read_json

model_config = read_json("squad_ru_bert_infer.json")
model = build_model(model_config, download=True)

tokenFile = open("token.txt")
token = tokenFile.readline().strip()

contextFile = open("context.txt")
context = contextFile.read()

bot = telebot.TeleBot(token)

@bot.message_handler(commands=["start"])
def send_welcome(message):
	bot.reply_to(message, "Привет! Ты можешь спросить у меня о ...")

@bot.message_handler(func=lambda message: True)
def echo_all(message):
	messageText = message.text
	print(messageText)
	answer = model([context], [messageText])
	print(answer)
	if answer[0][0].strip() and answer[2][0] == 1:
		bot.reply_to(message, answer)
	elif answer[2][0] < 1 and answer[0][0].strip():
		bot.reply_to(message, "Я не могу дать точный ответ на вопрос")
	else:
		bot.reply_to(message, "Вопрос не по тексту")

bot.infinity_polling()
