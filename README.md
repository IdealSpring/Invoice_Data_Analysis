## 介绍
------
* **开发原因**
> 实验室传统，每年都会参加“中国软件杯程序设计大赛”；以比赛为依托，学习计算机相关技术，用比赛锻炼个人能力和团队合作能力。本项目因此而被开发。
* **大赛名称**
> ["中国软件杯"大学生程序设计大赛](http://www.cnsoftbei.com/)
* **所选赛题**
> [基于企业进销项发票数据的异常企业预测分析](http://www.cnsoftbei.com/bencandy.php?fid=151&id=1610)
* **项目周期**
> 2018年02月08日---2018年09月02日，历时7个月。

## 开发目的
------
> 通过进销项增值税发票和纳税人信息，挖掘出其中潜在信息，预测其中存在问题的企业。

## 解决思路
------
* 项目(赛题)定位
> 大数据分析
* 解决方式
> 在大量发票数据中随机选取一定量的数据作为训练集和测试集，通过机器学习算法训练出预测模型，使用模型预测测试集数据；通过预测结果的F1-score判断模型的好坏。

## 流程
------
* 本项目第一阶段是对增值税发票数据进行特征提取；第二阶段是对企业ID及其特征属性构成的信息元来预测企业是否偷漏税，本过程想要使用已打标签的企业ID及其特征属性构成的信息元进行模型的训练，之后使用模型进行数据预测。
* 特征提取
> 通过进销项增值税发票数据和纳税人信息共计提取出[22个特征](https://github.com/IdealSpring/Invoice_Data_Analysis/blob/master/AttributeExtraction/%E3%80%90%E9%87%8D%E8%A6%81%E3%80%91%E6%8F%90%E5%8F%96%E5%B1%9E%E6%80%A7%E8%AF%B4%E6%98%8E_%5B%E5%BF%85%E7%9C%8B%5D/02_%E9%9A%8F%E6%9C%BA%E6%A3%AE%E6%9E%97%E4%BD%BF%E7%94%A8%E5%B1%9E%E6%80%A7.txt)
* 预测分析
> 使用随机森林算法、朴素贝叶斯算法、梯度提升术算法和神经网络算法4种算法分别训练模型并进行预测，然后将每个模型预测出的结果使用Bagging算法来获取最终预测结果。
* [项目位置](https://github.com/IdealSpring/Invoice_Data_Analysis/tree/master/PredictionAlgorithm/zhipeng_Tong)
## 最终测试集F1-score
------
> **F1-score=94.55%**

## 比赛提交作品完整包
------
> 链接：https://pan.baidu.com/s/1Z3rhNXn9Kf_5MBfEG1RqAA ，提取码：68th 

## 最后
------
这七个月是艰苦的，从除夕开始到比赛结束，从对大数据一无所知(每个名次都百度过)到最后95%的F1-score的预测指标。七个月来的风风雨雨，感谢我的两名队友与我相伴；每天晚上一起讨论问题和汇报自己的进展，两个人争论一个问题需要另一个人的仲裁……。我们这个team，一个人主要负责搭建基础建构、技术走向和管控整个项目；一个人有着别样的想法思路，每每提出一些想法，让人耳目一新豁然开朗；另一个人巧舌如簧、能答善辩、措辞标准，在最比决赛的舞台一展风采。还有实验室导师的耐心指导。感谢你们！
<div style="float:left;border:solid 1px 000;margin:2px;">
	<img src="https://github.com/IdealSpring/Invoice_Data_Analysis/blob/master/img/%E6%80%BB%E5%86%B3%E8%B5%9B%E7%85%A7%E7%89%87.jpg"  height="300" width="400" >
	<img src="https://github.com/IdealSpring/Invoice_Data_Analysis/blob/master/img/%E6%80%BB%E5%86%B3%E8%B5%9B%E7%85%A7%E7%89%87.jpg"  height="300" width="400" >
	<img src="https://github.com/IdealSpring/Invoice_Data_Analysis/blob/master/img/1.%E4%BA%8C%E7%AD%89%E5%A5%96%E5%A5%96%E7%89%8C.jpg"  height="200" width="280" >
	<img src="https://github.com/IdealSpring/Invoice_Data_Analysis/blob/master/img/2.%E6%9C%80%E4%BD%B3%E8%A1%A8%E7%8E%B0%E5%A5%96%E5%A5%96%E7%89%8C.jpg"  height="200" width="280" >
	<img src="https://github.com/IdealSpring/Invoice_Data_Analysis/blob/master/img/3%E4%BA%A7%E6%95%99%E8%9E%8D%E5%90%88%E5%88%9B%E6%96%B0%E5%A5%96.jpg"  height="200" width="280" >

</div>
