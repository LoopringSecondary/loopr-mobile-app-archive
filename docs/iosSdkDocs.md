# Loopring iOS SDK Spec
---

## web3swift Methods
---

### encode
Using the default Ethereum encoding method to encode a function that has a specific type of purpose i.e. transfer, withdraw, deposit, approve, etc.
##### Parameters
* **function** - a function that encapsulates the type of action and items such as address and/or token amount

##### Returns
* **EthFunctionEncoder.default.encode(function)** - encoded function with specific parameters and methodID

### sign
Used for transaction signatures. Takes keystore file to verify and decipher the private key to create a signature for transaction values. Also compares hash to verify the private key.
##### Parameters
* **keystore** - your keystore file
* **account** - your account
* **address** - reciever address
* **encodedFunctionData** - type of transaction
* **nonce** - a pseudo random number that can only be used once
* **amount** - amount inteded to trade
* **gasLimit** - the maximum amount of gas willing to pay
* **gasPrice** - the gas price used
* **password** - your password

##### Returns
* **finalTransaction** - a GethTransaction type that includes the signature data

### sign
Used for order or data buffer signatures. Takes keystore file to verify and decipher the private key to create a signature for transaction values. Also compares hash to verify the private key. However, only returns if the default values of keystore, account, and passphrase are defined.
##### Parameters
* **message** - message to be encrypted

##### Returns
* **signature** - signature data in hex (v, r, s)
* **hash** - hash code used for verification

---
## Relay API Methods
---

### getBalance
Get user's balance and token allowance info.
See [loopring_getBalance](https://github.com/Loopring/relay-cluster/blob/master/docs/relay_api_spec_v2.md#loopring_getbalance)

### getOrders
Get loopring order list.
See [loopring_getOrders](https://github.com/Loopring/relay-cluster/blob/master/docs/relay_api_spec_v2.md#loopring_getorders)

### getOrderByHash
Get loopring order by order hash.
See [loopring_getOrderByHash](https://github.com/Loopring/relay-cluster/blob/master/docs/relay_api_spec_v2.md#loopring_getorderbyhash)

### getDepth
Get depth and accuracy by token pair.
See
[loopring_getDepth](https://github.com/Loopring/relay-cluster/blob/master/docs/relay_api_spec_v2.md#loopring_getdepth)

### getTicker
Get info on Loopring's 24hr merged tickers from loopring relay.
See
[loopring_getTicker](https://github.com/Loopring/relay-cluster/blob/master/docs/relay_api_spec_v2.md#loopring_getticker)

### getTickers
Get the info on all the 24hr merged tickers in the market from loopring relay.
See
[loopring_getTickers](https://github.com/Loopring/relay-cluster/blob/master/docs/relay_api_spec_v2.md#loopring_gettickers)

### getFills
Get order fill history. This history consists of OrderFilled events.
See
[loopring_getFills](https://github.com/Loopring/relay-cluster/blob/master/docs/relay_api_spec_v2.md#loopring_getfills)

### getTrend
Get trend info per market. If you select 1Hr interval, this function will return a list(the length is 100 mostly). Each item represents a data point of the price change in 1Hr. The same goes for other intervals.
See
[loopring_getTrend](https://github.com/Loopring/relay-cluster/blob/master/docs/relay_api_spec_v2.md#loopring_gettrend)

### getRingMined
Get all mined rings.
See
[loopring_getRingMined](https://github.com/Loopring/relay-cluster/blob/master/docs/relay_api_spec_v2.md#loopring_getringmined)

### getCutoff
Get cut off time of the address.
See
[loopring_getCutoff](https://github.com/Loopring/relay-cluster/blob/master/docs/relay_api_spec_v2.md#loopring_getcutoff)

### getPriceQuote
Get the USD/CNY/BTC quoted price of tokens.
See
[loopring_getPriceQuote](https://github.com/Loopring/relay-cluster/blob/master/docs/relay_api_spec_v2.md#loopring_getpricequote)

### getEstimatedAllocatedAllowance
Get the total frozen amount of all unfinished orders.
See
[loopring_getEstimatedAllocatedAllowance](https://github.com/Loopring/relay-cluster/blob/master/docs/relay_api_spec_v2.md#loopring_getestimatedallocatedallowance)

### getSupportedTokens
Get all relay-supported tokens.
See
[loopring_getSupportedTokens](https://github.com/Loopring/relay-cluster/blob/master/docs/relay_api_spec_v2.md#loopring_getsupportedtokens)

### getSupportedMarket
Get all relay-supported market pairs.
See
[loopring_getSupportedMarket](https://github.com/Loopring/relay-cluster/blob/master/docs/relay_api_spec_v2.md#loopring_getsupportedmarket)

### getTransactions
Get user's latest transactions by owner.
See
[loopring_getTransactions](https://github.com/Loopring/relay-cluster/blob/master/docs/relay_api_spec_v2.md#loopring_gettransactions)

### unlockWallet
Tell the relay the unlocked wallet info.
See
[loopring_unlockWallet](https://github.com/Loopring/relay-cluster/blob/master/docs/relay_api_spec_v2.md#loopring_unlockwallet)

### getEstimateGasPrice
Get estimated gas price from Relay.
See
[loopring_getEstimateGasPrice](https://github.com/Loopring/relay-cluster/blob/master/docs/relay_api_spec_v2.md#loopring_getestimategasprice)

### getFrozenLRCFee
Get the total frozen lrcFee of all unfinished orders.
See
[loopring_getGetFrozenLRCFee](https://github.com/Loopring/relay-cluster/blob/master/docs/relay_api_spec_v2.md#loopring_getgetfrozenlrcfee)

### notifyTransactionSubmitted
Wallet should notify relay there was a transaction sending to eth network, then relay will get and save the pending transaction immediately.
See
[loopring_notifyTransactionSubmitted](https://github.com/Loopring/relay-cluster/blob/master/docs/relay_api_spec_v2.md#looprint_notifytransactionsubmitted)

### submitOrder
Submits an order. The order is submitted to the relay as a JSON object, which will be broadcasted into a peer-to-peer network for off-chain order-book maintainance and ring-ming. Once mined, the ring will be serialized into a transaction and submitted to the Ethereum blockchain.
See
[loopring_submitOrder](https://github.com/Loopring/relay-cluster/blob/master/docs/relay_api_spec_v2.md#loopring_submitorder)

### submitRing
Submit signed raw transaction of ring information, then relay can help submitting the ring while tracing the status of orders for wallet. please submit taker and maker order before invoking this method.
See
[loopring_submitRingForP2P](https://github.com/Loopring/relay-cluster/blob/master/docs/relay_api_spec_v2.md#loopring_submitringforp2p)

### getNonce
Get newest nonce of user's address, used on the pending transaction counts submitted to relay.
See
[loopring_getNonce](https://github.com/Loopring/relay-cluster/blob/master/docs/relay_api_spec_v2.md#loopring_getnonce)
