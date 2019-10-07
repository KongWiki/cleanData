## 数据清洗

### 程序架构

![image](/home/kongweikun/Pictures/数仓-报告-java框架.png)

* filter包

  ![imge](/home/kongweikun/Pictures/数仓-程序架构-filter包.png)

  在filter包中，CsvFilter 类定义了数据处理的模板方法，每个表只需处理和对应的 CSV 文件的名字以及每一
  个字段的规则就可以，核心的预处理方法为 processCsv()方法，方法的主要流程为：

  1. 根据子类定义的 getCsvName()获取当前要处理的 csv 文件，创建读取和写出类。
  2. 根据子类定义的 getRules()获取当前 csv 文件每一个字段的处理规则
  3. 循环读取每一行数据，对每一段数据应用规则，如果返回空值则舍弃这一行数据
  4. 将处理好的数据写入新文件，将丢弃的数据写入丢弃日志，便于手动修改

* rules包

  ![image](/home/kongweikun/Pictures/数仓-程序架构-rules.png)

  对所有现有的csv文件中的每个特征进行整理，然后整理了一些对应的规则，以下为主要的规则

  1. 生日、性别规则
  2. 日期规则
  3. 银行字段规则
  4. 还款期数规则
  5. 枚举字段规则
  6. 普通字符串规则
  7. 数字规则
  8. ID字段规则

  有一些规则会根据字段的具体情况进行更具体的规则。

* utils包

  主要用于csv的读取，导入了javacsv包，对csv文件进行读取。

### 清洗规则

1. 生日、性别规则

   原 CSV 文件使用了特殊的生日表示法，若客户为女性，则月数表示的时候加 50，这一字段需要拆分为两个字段，为正常显示的生日日期和性别字段，譬如565313表示为1956年3月13日，同时对与该规则的处理还添加了三个特征，一个是isMale用于标明是性别，一个是age用于计算年龄，还有一个是ageType，为age/10，不超过6

2. 日期规则

   日期主要是统一了输出的格式为yyy-MM-dd  HH:mm:ss，使用 DateFormat 尝试将 csv 字段中的字符串转换为 Date 类型，如果转换失败则丢弃这条字段。

3. 枚举字段规则

   其主要查找该字段和已经定的字段是否相等，若是相等，则返回翻译后的字段，若是不相等，则使用编辑距离算法计算该字段和已经定义的所有字段的相似度，取最为相近的字段，字段修复之后，对现有的字段进行翻译，翻译如下：

   ![imaeg](/home/kongweikun/Pictures/数仓-清洗规则-枚举.png)

4. 数字规则

   统一使用整数的转换方法 Integer.parseInt(item)，若跑出异常错误，则丢弃该数据，正常则检测对应的每一位是否为数字类型

以上为前期的第一次的数据预清洗，只是单单的考虑每个字段的合法性，并没有考虑字段自检的逻辑和字段整体之间的合法性。所以需要在数据入库之后，使用SQL检查字段之间的合法性，其主要包括主键的唯一性、外键的合法性、字段之间的逻辑合法性。

## 数据导入

### 导入后的数据的再次清洗

#### 主键的唯一性

八个数据表中，只有account和card表出现了 问题

**account表 检查每个ID是否有重复**

![image](/home/kongweikun/Pictures/数仓-主键唯一性-account.png)

#### 外键合法性

**account与其他的表的对应**

* account - disp

  ![image](/home/kongweikun/Pictures/数仓=外键合法性-disp-account.png)

* account  - loan

  ![image](/home/kongweikun/Pictures/数仓-外键合法性-loan-account.png)

* account - order

  ![image](/home/kongweikun/Pictures/数仓-外键合法性-order-account.png)

* account - trans 比较多

  ![image](/home/kongweikun/Pictures/数仓-外键合法性-trans-account.png)

  ![image](/home/kongweikun/Pictures/数仓-外键合法性-trans-accountII.png)

**district与其他的表的对应**

* district - client

  ![image](/home/kongweikun/Pictures/数仓-外键合法性-disp-client.png)

* district - other

  ![image](/home/kongweikun/Pictures/数仓-外键合法性-other-district.png)

**disp与其他的表的对应**

* disp - other

  ![image](/home/kongweikun/Pictures/数仓-外键合法性-card-disp.png)

**字段与字段之间的逻辑关系**

![image](/home/kongweikun/Pictures/数仓-数据预处理.png)

### 完善表格结构

最后处理字段之间的合法性之后的，关系如下图，原始表最终确定为8张：

![image](/home/kongweikun/Pictures/数仓-表结构.png)

## 维度表和事实表的建立

### 维度表(创建了11张表)

主要为在原始表的基础上提取部分字段，再次进行划分，程序如下

```python
class Dimension(object):
	def __init__(self, user, password, host, db, port):
		"""
		初始化链接mysql的
		:param user:
		:param password:
		:param host:
		:param db:
		:param port:
		"""
		self.user = user
		self.password = password
		self.host = host
		self.datbase = db
		self.port = port

		self.dbm = pymysql.connect(host=self.host,
								   db=self.datbase,
								   user=self.user,
								   password=self.password,
								   port=self.port,
								   charset='utf8mb4')

		self.cursor = self.dbm.cursor()
    
     def _orderKDminension(self):
		try:
			# 用于创建(orderK_dimension 维度的sql (orderK_id orderK_chinese, orderK_value)
			orderKSQL = 'INSERT INTO orderK_dimension VALUES (%d, "%s", "%s")'
			# 从order中order_id amount
			orderSQL = "SELECT k_symbol FROM orderTable"
			self.cursor.execute(orderSQL)
			result = self.cursor.fetchall()
			idDict = {'INSURANCE': 1, 'MANAGEMENT': 2,
					  'RENTAL': 3, 'LOAN': 4, '': 5}
			chineseDict = {'INSURANCE': '保险费支付', 'MANAGEMENT': '物业管理支付',
						   'RENTAL': '租金缴纳', 'LOAN': '偿还贷款', '': '未知'}
			count = 0
			for i in result:
				chinese = chineseDict[i[0]]
				id = idDict[i[0]]
				print(orderKSQL % (id, chinese, i[0]))
				self.cursor.execute(orderKSQL % (id, chinese, i[0]))
				count += 1
		# self.dbm.commit()
		finally:
			self.dbm.close()
		print("收款订单类型 维度创建成功 共{}条数据".format(count))
     ....
   
```



#### 年份维度(year_dimension)

| 列名       | 数据类型    | 含义   |
| ---------- | ----------- | ------ |
| year_id    | int         | 年份id |
| year_value | varchar(50) | 年份值 |

#### 年龄纬度(age_dimension)

| 列名          | 数据类型     | 含义                      |
| ------------- | ------------ | ------------------------- |
| age_id        | int          | 年龄维度id                |
| age_value     | varchar(50)  | 年龄的范围解释，如‘10-20’ |
| age_group     | varchart(50) | 所属的群体，如老年、青年  |
| age_client_id | int          | 对应id                    |

**分类的详细信息为:**

| age_id | age_value | age_group |
| ------ | --------- | --------- |
| 0      | 0~9       | 小孩      |
| 1      | 10-19     | 少年      |
| 2      | 20~29     | 青年      |
| 3      | 30~39     | 壮年      |
| 4      | 40~49     | 壮年      |
| 5      | 50~59     | 壮年      |
| 6      | 60~69     | 老年      |

#### 信用卡类型维度(card_dimension)

| 列名         | 数据类型    | 含义                     |
| ------------ | ----------- | ------------------------ |
| card_id      | int         | 维度id                   |
| card_chinese | varchar(50) | 信用卡类型中文翻译       |
| card_value   | varchar(50) | 信用卡类型：金卡、普通卡 |

**分类详细信息**

| card_id | card_chinese | card_value |
| ------- | ------------ | ---------- |
| 1       | GOLD         | 金卡       |
| 2       | CLASSIC      | 普通卡     |
| 3       | JUNIOR       | 初级卡     |

![image](/home/kongweikun/Pictures/数仓-维度表-信用卡类类型纬度.png)

#### 性别维度(gender_dimension)

| 列名             | 数据类型    | 含义               |
| ---------------- | ----------- | ------------------ |
| gender_id        | int         | 维度id             |
| gender_value     | varchar(50) | 性别值：男，女     |
| gender_client_id | int         | 与client表相关的id |

#### 操作数据类型维度(operation_dimension)

| 列名               | 数据类型    | 含义                         |
| ------------------ | ----------- | ---------------------------- |
| operation_id       | int         | 维度id                       |
| operation_chinese  | varchar(50) | 操作类型翻译                 |
| operation_value    | varchar(50) | 操作类型值：信用卡取现、、、 |
| operation_trans_id | int         | 对应id                       |

**分类的详细信息为：**

| opration_id | operation_value | operation_chinese |
| ----------- | --------------- | ----------------- |
| 1           | FROM_OTHER_BANK | 其他银行汇款      |
| 2           | CASH            | 提取现金          |
| 3           | TO_OTHER_BANK   | 汇款到其他银行    |

![image](/home/kongweikun/Pictures/数仓-维度表-操作数据类型维度表.png)

#### 收款订单额维度(order_amount_dimension)

| 列名            | 数据类型    | 含义                         |
| --------------- | ----------- | ---------------------------- |
| order_amount_id | int         | 维度id                       |
| amount_value    | varchar(50) | 订单额/1000的数值，不超过11  |
| amount_group    | varchar(50) | 对订单额的描述，如很少，很多 |

**分类的详细信息为：**

| order_amount_id | amount_value | amount_group |
| --------------- | ------------ | ------------ |
| -1              | <=-500       | 取款         |
| 0               | -499-499     | 较少         |
| 1               | 500~1499     | 较少         |
| 2               | 1500~2499    | 中等         |
| 3               | 2500~3499    | 中等         |
| 4               | 3500~4499    | 中等         |
| 5               | 4500~5499    | 较多         |
| 6               | 5500~6499    | 较多         |
| 7               | 6500~7499    | 较多         |
| 8               | 7500~8499    | 很多         |
| 9               | 8500~9499    | 很多         |
| 10              | 9500~10499   | 很多         |
| 11              | >=10500      | 土豪         |

#### 收款订单类型维度(orderK_dimension)

| 列名           | 数据类型    | 含义             |
| -------------- | ----------- | ---------------- |
| orderK_id      | int         | 维度id           |
| orderK_chinese | varchar(50) | 订单类型中文翻译 |
| orderK_value   | varchar(50) | 订单类型值       |

**分类的详细信息为：**

| orderK_id | orderK_value | orderK_chinese |
| :-------- | ------------ | -------------- |
| 1         | INSURANCE    | 保险费支付     |
| 2         | MANAGEMENT   | 物业管理支付   |
| 3         | RENTAL       | 租金缴纳       |
| 4         | LOAN         | 偿还贷款       |
| 5         |              | 未知           |

![image](/home/kongweikun/Pictures/数仓-维度表-收款订单类型维度.png)

#### 地区维度(region_dimension)

| 列名          | 数据类型    | 含义                       |
| ------------- | ----------- | -------------------------- |
| region_id     | int         | 地区维度id 对应district_id |
| district_name | varchar(50) | 地区名                     |
| region_name   | varchar(50) | 所属大区名                 |

#### 交易类型维度(trank_dimension)

| 列名          | 数据类型    | 含义               |
| ------------- | ----------- | ------------------ |
| trank_id      | int         | 维度id             |
| trank_chinese | varchar(50) | 交易类型值中文翻译 |
| trank_value   | varchar(50) | 交易类型值         |

**分类的详细信息为:**

| trank_id |   trank_value   | trank_chinese |
| :------: | :-------------: | :-----------: |
|    1     |    INSURANCE    |  保险费支付   |
|    2     |    HOUSEHOLD    |  物业管理费   |
|    3     | OLD_AGE_PERSION |    养老金     |
|    4     |      LOAN       |     还贷      |
|    5     |                 |     未知      |

![image](/home/kongweikun/Pictures/数仓-维度表-交易额维度.png)

#### 交易额维度(trans_amount_dimension)

| 列名            | 数据类型    | 含义                              |
| --------------- | ----------- | --------------------------------- |
| trans_amount_id | int         | 维度 id                           |
| amount_value    | varchar(50) | 交易额/1000 的数值,不超过 11      |
| amount_group    | varchar(50) | 对交易额的描述,如很少、很多...... |

**分类的详细信息为：**

| order_amount_id | amount_value | amount_group |
| --------------- | ------------ | ------------ |
| -1              | <=-500       | 取款         |
| 0               | -499-499     | 较少         |
| 1               | 500~1499     | 较少         |
| 2               | 1500~2499    | 中等         |
| 3               | 2500~3499    | 中等         |
| 4               | 3500~4499    | 中等         |
| 5               | 4500~5499    | 较多         |
| 6               | 5500~6499    | 较多         |
| 7               | 6500~7499    | 较多         |
| 8               | 7500~8499    | 很多         |
| 9               | 8500~9499    | 很多         |
| 10              | 9500~10499   | 很多         |
| 11              | >=15000      | 土豪         |

#### 余额维度(trans_balance_dimension)

| 列名             | 数据类型    | 含义                            |
| ---------------- | ----------- | ------------------------------- |
| trans_balance_id | int         | 维度 id                         |
| balance_value    | varchar(50) | 余额/1000 的数值,不超过 11      |
| balance_group    | varchar(50) | 对余额的描述,如很少、很多...... |

**分类的详细信息为:**

| trans_balance_id | balance_value | balance_group |
| ---------------- | ------------- | ------------- |
| -1               | <=-5000       | 负债          |
| 0                | -4990~4990    | 较少          |
| 1                | 5000~14990    | 中等          |
| 2                | 15000~24990   | 中等          |
| 3                | 25000~34990   | 中等          |
| 4                | 35000~44990   | 中等          |
| 5                | 45000+        | 较多          |

### 事实表(新创建2个视图)

#### 用户数量事实表(user_number)

```sql
CREATE VIEW gender_client AS 
SELECT client.client_id , client.birth_number , client.district_id , client.gender , client.age, client.ageType ,gender_dimension.gender_id , gender_dimension.gender_value 
FROM client, gender_dimension  
WHERE gender_dimension.gender_client_id = client.client_id;

CREATE VIEW card_card AS
SELECT card.card_id , card.disp_id , card.type , card.issued , card_dimension.card_id , card_dimension.card_chinese , card_dimension.card_value , card_card_id 
FROM card, card_dimension  
WHERE card.card_id = card_dimension.card_card_id;


SELECT gender_client.gender_value, card_card.card_chinese, account.dateYear , COUNT(*) AS user_number 
FROM disp 
INNER JOIN account ON account.account_id = disp.account_id  
INNER JOIN gender_client ON gender_client.client_id = disp.client_id  
INNER JOIN card_card ON card_card.disp_id = disp.disp_id 
GROUP BY gender_client.gender_value , card_card.card_chinese, account.dateYear;
```

![image](/home/kongweikun/Pictures/数仓-事实表-user_num.png)

#### 操作金额事实表(amount_value)

```sql

<--age-->
CREATE VIEW age_client AS SELECT client.client_id , client.birth_number , client.district_id , client.gender , client.age, client.ageType , age_dimension.age_id , age_dimension.age_value , age_dimension.age_group , age_dimension.age_client_id  FROM client, age_dimension WHERE client.client_id  = age_dimension.age_client_id;

<--age gender-->
CREATE VIEW age_gender_client AS SELECT age_client.client_id , age_client.birth_number , age_client.district_id , age_client.gender , age_client.age, age_client.ageType, age_client.age_id, age_client.age_value, age_client.age_group, age_client.age_client_id , gender_client.gender_id , gender_client.gender_value  FROM age_client, gender_client WHERE age_client.client_id  = gender_client.client_id;

<--age gender region-->
CREATE VIEW age_gender_client_region AS SELECT age_gender_client.client_id, age_gender_client.birth_number, age_gender_client.district_id, age_gender_client.gender, age_gender_client.age, age_gender_client.ageType, age_gender_client.age_id, age_gender_client.age_value, age_gender_client.age_group, age_gender_client.age_client_id, age_gender_client.gender_id, age_gender_client.gender_value, region_dimension.region_id , region_dimension.district_name , region_dimension.region_name  FROM region_dimension, age_gender_client WHERE region_dimension.region_id = age_gender_client.district_id;


<--operation_trans-->
CREATE VIEW operation_trans AS 
SELECT operation_trans.operation_id , operation_trans.operation_chinese , operation_trans.operation_value , operation_trans.operation_trans_id , operation_trans.trans_id , operation_trans.account_id , operation_trans.date , operation_trans.type , operation_trans.operation, operation_trans.amount , operation_trans.balance , operation_trans.k_symbol , operation_trans.bank , operation_trans.account, disp.disp_id, disp.client_id , disp.account_id , disp.type  FROM operation_trans, disp WHERE disp.account_id = operation_trans.account_id 

<--opration_trans_disp-->
CREATE VIEW operation_trans_disp AS SELECT operation_trans.operation_id , operation_trans.operation_chinese , operation_trans.operation_value , operation_trans.operation_trans_id , operation_trans.trans_id , operation_trans.account_id as operatino_trasn_disp_account_id , operation_trans.date , operation_trans.type , operation_trans.operation, operation_trans.amount , operation_trans.balance , operation_trans.k_symbol , operation_trans.bank , operation_trans.account, disp.disp_id, disp.client_id , disp.account_id , disp.type as operation_trans_disp_type  FROM operation_trans, disp WHERE disp.account_id = operation_trans.account_id;

```

![image](/home/kongweikun/Pictures/数仓-事实表-amount-total_II-1.png)

![image](/home/kongweikun/Pictures/数仓-事实表-amount_total_II-2.png)

#### 贷款金额(loan)

执行7分30秒

```sql
CREATE VIEW loan_analytic AS SELECT gender_dimension.gender_id , card_dimension.card_id , age_dimension.age_id , region_dimension.region_id , order_amount_dimension.order_amount_id , trans_amount_dimension.trans_amount_id , trans_balance_dimension.trans_balance_id , trank_dimension.trank_id , orderK_dimension.orderK_id, AVG(loan.amount ) AS amount, SUM(loan.amount ) AS sum_amount  FROM  loan INNER JOIN account ON account.account_id = loan.account_id  INNER JOIN disp ON disp.account_id = account.account_id  INNER JOIN client ON client.client_id =  disp.client_id  INNER JOIN district ON district.district_id = client.district_id  INNER JOIN card ON card.disp_id = disp.disp_id  INNER JOIN trans ON trans.account_id = account.account_id  INNER JOIN orderTable ON orderTable.account_id = account.account_id INNER JOIN age_dimension ON age_dimension.age_client_id = client.client_id  INNER JOIN card_dimension ON card_dimension.card_card_id = card.card_id  INNER JOIN gender_dimension ON gender_dimension.gender_client_id = client.client_id  INNER JOIN order_amount_dimension ON order_amount_dimension.order_amount_order_id = orderTable.order_id  INNER JOIN region_dimension ON region_dimension.region_id = district.district_id  INNER JOIN trans_amount_dimension ON trans_amount_dimension.trans_amount_trans_id = trans.trans_id  INNER JOIN trans_balance_dimension ON trans_balance_dimension.trans_balance_trans_id = trans.trans_id  INNER JOIN orderK_dimension ON orderK_dimension.orderK_order_id = orderTable.order_id  INNER JOIN trank_dimension ON trank_dimension.trank_trans_id = trans.trans_id WHERE (disp.type <> 'DISPONENT')  GROUP by gender_dimension.gender_id , age_dimension.age_id , card_dimension.card_id , order_amount_dimension.order_amount_id , region_dimension.region_id;

```

![image](/home/kongweikun/Pictures/数仓-事实表-贷款金额.png)



### 表数量

最后，共创建了35张表，其中视图有两张，为事实表。

![image](/home/kongweikun/Pictures/数仓-表总量.png)

### 分析

对不同的度量值，使用不同的维度来度量，不同的度量值组和维度的关系如下

![image](/home/kongweikun/Pictures/数仓-分析-维度-度量.png)

#### 用户数量

![image](/home/kongweikun/Pictures/数仓-分析-usr-num.png)

* 1993年开通账户的女性中，持有金卡的有15人
* 1996年开通普通卡的女性最多为114人，总体来说1996年开通账户的顾客持有的信用卡的人数最多。
* 开卡数量统计上，男性多余女性，开通普通账户客户中持有普通信用卡的最多。

#### 操作金额

##### **一维 --- gender**

![image](/home/kongweikun/Pictures/数仓-事实表-金额-一维.png)

![image](/home/kongweikun/Pictures/数仓-一维.png)

- 汇款到其他银行： 男性操作的多，并且均值也大
- 其他银行汇款： 女性的多，但是均值上还是男性较多
- 提取现金： 女性均超过男性

##### **二维 --- gender-ageGroup** 

![image](/home/kongweikun/Pictures/数仓-事实表-二维-1.png)

* 壮年和老年人交易总金额和平均值更多，壮年客户汇款到其他银行的数额和均值都远超过老年人，而老年人收到其他银行汇款总额和均值都远远超过壮年客户

![image](/home/kongweikun/Pictures/数仓-事实表-二维-2-3条.png)

* 男性壮年客户汇出了更多的金额
* 女性老年客户收到了最多的其他银行汇款

![image](/home/kongweikun/Pictures/数仓-事实表-二维-4.png)

* 男性壮年提取了更多的现金，但女性壮年客户提取现金的平均值更多



##### **三维 --- gender-ageGroup-region**

![iame](/home/kongweikun/Pictures/数仓-事实表-三维-1.png)

North Moravia在汇款和收款两项中，都高于其他的地区，South Bohemia和West Bohemia相比而言就比较少。

对North Moravia进行详细的分析

![image](/home/kongweikun/Pictures/数仓-三维-2.png)

![image](/home/kongweikun/Pictures/数仓-三维-3.png)

来自North Moravia的女性壮年客户汇款到其他银行的总额更多，但是North Moravia的男性壮年汇款到其他银行的平均值更大。整体来看，汇款到其他银行和其他一行汇款占比大，现金提取少。

#### 贷款总额

##### 年龄性别

![image](/home/kongweikun/Pictures/数仓-贷款-年龄-性别.png)

20-29的男性贷款金额多，然后整体随着年龄的增长，贷款总额呈下降趋势，50-59的女性例外

##### 信用卡

![image](/home/kongweikun/Pictures/数仓-贷款金额-gender-age-cardII.png)

从持卡人信用卡的情况可以看出持有初级卡的男性在20-29的客户贷款额异常的高，远远超出其他群体

##### 交易数据

![image](/home/kongweikun/Pictures/数仓-贷款金额-交易书.png)

不同的性别，其贷款额较高的使用方向是偿还贷款，，其中女性偿还贷款的程度高于男性。



##### 余额

![image](/home/kongweikun/Pictures/数仓-贷款金额-余额.png)

从账户余额上来看，账户余额在 45000+ 的 20~29 岁男性客户贷款额更多,余额在5000~44990的30-39的女性的贷款量较多。

男性，20-29，持有初级或者普通信用卡，需要偿还中等金额贷款，且余额在45000＋的

女性，20-39，持有初级或者普通信用卡，需要偿还较多金额贷款，且余额在5000~44990的

对于以上特征的人，应该加大宣传力度。



