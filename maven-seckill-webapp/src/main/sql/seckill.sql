-- ��ɱִ�еĴ洢����
DELIMITER $$ -- console ; ת��Ϊ $$
-- ����洢����
-- ���� : in ��ʾ�������; out ��ʾ�������
-- row_count() ���������� : ������һ���޸����͵� sql(delete��update��insert) ��Ӱ������
-- insert_count : 0 : δ�޸�����;   >0 : ��ʾ�޸ĵ����� ;   <0 : ��ʾsql����/δִ���޸�sql 
-- -1 ��ʾ��Ϊ�ظ���ɱ; -2 ��ʾϵͳ�쳣; 0 ��ʾ��ɱ����; 

CREATE PROCEDURE execute_seckill
	(in v_seckill_id bigint, in v_phone bigint,
	 in v_kill_time timestamp, out r_result int)
	BEGIN
		DECLARE insert_count int DEFAULT 0;
		START TRANSACTION;
		insert ignore into success_killed
			(seckill_id, user_phone, state, create_time)
		values(v_seckill_id, v_phone, 0, v_kill_time);
		
		select row_count() into insert_count;
		IF (insert_count = 0) THEN
			ROLLBACK;
			SET r_result = -1;
		ELSEIF (insert_count < 0) THEN
			ROLLBACK;
			SET r_result = -2;
		ELSE
			update seckill
			set number = number - 1
			where seckill_id = v_seckill_id			
				and start_time < v_kill_time
				and end_time >= v_kill_time
				and number > 0;
			
			select row_count() into insert_count;
			IF (insert_count = 0) THEN
				ROLLBACK;
				SET r_result = 0;
			ELSEIF (insert_count < 0) THEN
				ROLLBACK;
				SET r_result = -2;
			ELSE
				COMMIT;
				set r_result = 1;
			END IF;
		END IF;
	END;
$$
-- �洢���̶������

-- չʾ�洢����
show create procedure execute_seckill;

DELIMITER ;

set @r_result=-3
-- ִ�д洢����
call execute_seckill(1002, 13245785555, now(), @r_result);
-- ��ȡ���
select @r_result;
	
-- �洢����
-- 1. �洢�����Ż� : �����м������е�ʱ��
-- 2. ��Ҫ���������洢����
-- 3. �򵥵��߼�����Ӧ�ô洢����
-- 4. QPS : һ����ɱ�� 600/QPS





