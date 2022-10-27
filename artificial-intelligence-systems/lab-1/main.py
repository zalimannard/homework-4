import telebot
import sys
from deeppavlov import build_model, configs
from deeppavlov.core.common.file import read_json

f = open("token.txt")
token = f.readline()
bot = telebot.TeleBot(token)

@bot.message_handler(commands=['start', 'help'])
def send_welcome(message):
	bot.reply_to(message, "Помощь")

@bot.message_handler(func=lambda message: True)
def echo_all(message):
	bot.reply_to(message, message.text)

bot.infinity_polling()
