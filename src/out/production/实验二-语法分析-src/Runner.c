int a[510];
int prime[510];
const int reflect1[16] = {-711942876, -1060809599, 1758839997, 423174272,
	1412407466, 375872692, 1543568839, 915987033,
	698198080, -2143283456, 2059223660, -34179219,
	378910912, 1498631475, -1853883889, 1640319187
};
const int reflect2[5][5] = {
	{-1152785601, 1891446969, 938484211, -1596516698, 1681072330},
	{128222498, 1090169129, -347746822, -326257601, -1366930863},
	{-823961496, 2103453081, -402114823, 2139806715, -732814375},
	{1302967469, 1623817872, 898372270, -812618050, -1403178881},
	{-1776328495, 958769364, 47496017, -1384738865, 1751940200}
};
int main() {
	int i = 2, total;
	total = 200;
	printf("19373354\n");
	while (i < total) {{{}{}
		a[i] = 1;
		i = i + 1;
	{}{}}}
	i = 2;
	int con = 0;
	while (i < total) {{{}{}
		if (a[i]) {{{}{}
			prime[con] = i;
			con = con + 1;
		{}{}}}
		int j = 0;
		while (j < con && i * prime[j] <= total) {{{}{}
			a[i * prime[j]] = 0;
			if (i % prime[j] == 0) break;
			j = j + 1;
		{}{}}}
		i = i + 1;
	{}{}}}
	i = 0;
	while (114514) {{{}{}
		if (a[i]) printf("%d %d\n", i, (i % 2) * reflect1[i % 16]
			+ ((i + 1) % 2) * reflect2[i % 5][i % 5]);
		i = i + 1;;;;;;;;;;;;;;;
		if (i < total) continue;
		else break;;;;;;;;;;;;;;
		continue;;;;;;;;;;;;;;;;
		break;;;;;;;;;;;;;;;;;;;
		continue;;;;;;;;;;;;;;;;
		break;;;;;;;;;;;;;;;;;;;
		continue;;;;;;;;;;;;;;;;
		break;;;;;;;;;;;;;;;;;;;
		continue;;;;;;;;;;;;;;;;
		break;;;;;;;;;;;;;;;;;;;
	{}{}}}
	return 0;
}
