-- KEYS[1]: 商品库存的 Key (例如: goods:stock:1001)
-- ARGV[1]: 购买的数量 (例如: 1)

-- 1. 从 Redis 中获取当前库存
local currentStock = redis.call('get', KEYS[1])

-- 2. 如果 Key 不存在（说明商品还没上架或者输错了 Key）
if currentStock == false then
    return -1  -- 返回 -1 代表商品不存在
end

-- 3. 将取出来的库存字符串转成数字，并与购买量做对比
if tonumber(currentStock) < tonumber(ARGV[1]) then
    return 0  -- 返回 0 代表库存不足，秒杀失败
else
    -- 4. 库存充足，扣减库存
    redis.call('decrby', KEYS[1], ARGV[1])
    return 1  -- 返回 1 代表扣减成功，抢到了！
end
