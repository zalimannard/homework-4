import telebot
import sys
from deeppavlov import build_model, configs
from deeppavlov.core.common.file import read_json

print(sys.argv)

model_config = read_json("squad_ru_bert_infer.json")
model = build_model(model_config, download=True)

botTokenFile = open("botToken.txt")
botToken = botTokenFile.readline()

contextFile = open("text-about-bashkortostan.txt")
context = contextFile.read()

bot = telebot.TeleBot(botToken)

answer = model([context], [sys.argv[2]])
print(answer)
if answer[0][0].strip() and answer[2][0] > 500:
    bot.send_message(sys.argv[1], answer)
    bot.reply_to(message, answer)
else:
    bot.send_message(sys.argv[1], "Мин һине аңламаным")
