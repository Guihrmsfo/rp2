# -*- coding: utf-8 -*-

from nltk.tokenize import TweetTokenizer
from nltk.corpus import stopwords
from collections import Counter
from nltk.stem.porter import PorterStemmer
from datetime import datetime

import operator
import json
import string
import pprint
import sys
import re

tknzr = TweetTokenizer(reduce_len=True)
result = [];

punctuation = list(string.punctuation)
stop = stopwords.words('english') + punctuation + ['rt', 'via', 'RT', '’', '…', 'The', '•']
count_all = Counter()
stemmer = PorterStemmer()

data = datetime.now().strftime('%d-%m-%Y')

with open(sys.argv[1] + '/' + data + '/' + sys.argv[2] + '/tweets.json', 'r', encoding=('utf8'), errors='replace') as f:
    for line in f:
        tweets = json.loads(line)
        for tweet in tweets:
	        tokens = tweet['text']
	        processed = tknzr.tokenize(tokens)
	        terms_stop = [term for term in processed if term not in stop]
	        count_all.update(terms_stop)
	        result.append(terms_stop)

results = [];
line = [];

def remove_emoji(data):
    """
    去除表情
    :param data:
    :return:
    """
    if not data:
        return data
    try:
    # UCS-4
        patt = re.compile(u'([\U00002600-\U000027BF])|([\U0001f300-\U0001f64F])|([\U0001f680-\U0001f6FF])')
    except re.error:
    # UCS-2
        patt = re.compile(u'([\u2600-\u27BF])|([\uD83C][\uDF00-\uDFFF])|([\uD83D][\uDC00-\uDE4F])|([\uD83D][\uDE80-\uDEFF])')
    return patt.sub('', data)

emoji_pattern = re.compile(r'\d+(.*?)[\u263a-\U0001f645]')

for tweet in result:
	if line:
		results.append(line)
	line = []
	for word in tweet:
		word = stemmer.stem(word)
		word
		line.append(word)


savedFile = sys.argv[1] + '/' + data + '/' + sys.argv[2] + '/words.txt'
f = open(savedFile,'w', encoding=('utf8'))
for linha in results:
	#f.write(emoji_pattern.sub(r'',(' '.join(linha))))
	f.write(remove_emoji(' '.join(linha)))
f.close()

#print(results)