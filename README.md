Парсер для карточек товара с сайта https://pitergsm.ru/

1) Запустить сервер в IDE (FinalprojectApplication.java)
2) Выбрать карточки товаров на https://pitergsm.ru/
3) Открыть сайт http://localhost:8080/
4) В строку ввести нужные ссылки, разделяя их запятыми : Пример - https://pitergsm.ru/catalog/phones/iphone/iphone-17/esim/122314/,https://pitergsm.ru/catalog/audio/smart-speakers/yandex/yandeks-stantsiya-strit/102167/,https://pitergsm.ru/catalog/accessories/chekhly-i-zashchita/chekhly/114476/
5) Чтобы получить JSON с результатами работы нужно открыть http://localhost:8080/allParsed

Примечания:
- Если заново отправить ссылки, то все потоки остановятся и запустятся с новыми ссылками
- Если в БД пусто, то ссылка с результатами не откроется
