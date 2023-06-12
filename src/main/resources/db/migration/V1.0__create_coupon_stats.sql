CREATE TABLE IF NOT EXISTS public.coupon_stats
(
  item_id                   VARCHAR(100),
  cant                      INT
);
ALTER TABLE coupon_stats ADD CONSTRAINT uk_item_id UNIQUE(item_id);