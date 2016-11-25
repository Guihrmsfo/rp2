from nltk.tokenize import TweetTokenizer
from nltk.corpus import stopwords
from collections import Counter
from nltk.stem.porter import PorterStemmer

import operator
import json
import string
import pprint
import sys

tknzr = TweetTokenizer(reduce_len=True)
result = [];

punctuation = list(string.punctuation)
stop = stopwords.words('english') + punctuation + ['rt', 'via', 'RT', '’', '…', 'The', '•']
count_all = Counter()
stemmer = PorterStemmer()

with open(sys.argv[1], 'r', encoding="utf8", errors='ignore') as f:
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

for tweet in result:
	if line:
		results.append(line)
	line = []
	for word in tweet:
		word = stemmer.stem(word)
		line.append(word)

f = open('tweetsProcessados.txt','w', encoding='utf8')
for linha in results:
	f.write(' '.join(linha))
f.close()

print(results)