# SLRE
Codes and datasets for "Knowledge Graph Embedding Preserving Soft Logical Regularity" (CIKM-2020)

## Introduction
The repository provides java implementations and two datasets used for this paper:
* [Knowledge Graph Embedding Preserving Soft Logical Regularity] Shu Guo, Lin Li, Zhen Hui, Lingshuai Meng, Bingnan Ma, Wei Liu, Lihong Wang, Haibin Zhai, and Hong Zhang. CIKM 2020.

## Datasets
The FB15K data set consists of 1,345 relations and 14,951 entities among them.
The training set contains 483,142 triples, the validation set 50,000 triples,
and the test set 59,071 triples. 693 soft rules are created for FB15K.

The DB100K data set consists of 470 relations and 99,604 entities among them.
The training set contains 597,572 triples, the validation set 50,000 triples,
and the test set 50,000 triples. 99 soft rules are created for DB100K.

All triples are unique and we make sure that all entities/relations appearing in
the validation or test sets were occurring in the training set.

### Files
Datasets we used are in the corresponding subfolder contained in datasets/ with the following formats:
* _train.txt, _valid.txt, _test.txt; training, valid, test set with string id; format: **e1**\t**r**\t**e2**\n
* RulePaths.txt; soft rule constraints; format: **r1,r2**\t**confidence**\n, or **r1,r2,r3**\t**confidence**\n, where '-' denotes the inversion
### Preprocessing
Directly run the following codes, so as to transform the string data to the one of digital form:
```
ChangeDataString2ID.java
ChangeRuleString2ID.java
``` 
After running the codes, we can obtain the corresponding datasets and rules with digital form:
* train.txt, valid.txt, test.txt; training, valid, test set with digital id
* RulePathsID.txt; soft rule constraints with digital id;
## Codes
### Run the training code 
```
java -jar run.jar -train datasets/FB15K/train.txt -valid datasets/FB15K/valid.txt -test datasets/FB15K/test.txt -all datasets/FB15K/all.txt -rule datasets/FB15K/RulePathsID.txt -m 1345 -n 14951 -k 200 -neg 10 -lmbda 0.01 -mu 0.0001 -gamma 0.5 -cons 0.01 -# 1000 -skip 50
```
### Parameters
You can change the hyperparameters when training the model
```
  - train: the path of training triples 
  - valid: the path of validate triples 
  - test: the path of testing triples
  - all: the path of all training, validate, and testing triples (copied from train/valid/test)   
  - rule: the path of rules (digital form)
  - m: number of relations 
  - n: number of entities 
  - k: embedding dimensionality 
  - neg: number of negative samples
  - lmbda: L2 regularization coefficient 
  - mu: regularization coffecient for rules
  - gamma: initial learning rate
  - cons: scale factor of SLRE
  - #: number of iterations  
  - skip: number of skipped iterations 
```
## Citation
```
@inproceedings{guo2020:slre,
	author = {Guo, Shu and Li, Lin and Hui, Zhen and Meng, Lingshuai and Ma, Bingnan and Liu, Wei and Wang, Lihong and Zhai, Haibin and Zhang, Hong},
	booktitle = {29TH ACM International Conference on Information and Knowledge Management},
	title = {Knowledge Graph Embedding Preserving Soft Logical Regularity},
	year = {2020}
}
```
## Contact
For all remarks or questions please contact Shu Guo: guoshu (at) cert (dot) org (dot) cn.