-- 입법예고 리스트
CREATE MATERIALIZED VIEW legislative_notices_mat AS
SELECT ln.id                                                 AS notice_id,
       b.bill_id,
       b.title                                               AS bill_title,
       b.age,
       b.propose_date,
       b.detail_link,
       ln.opinion_url,
       ln.start_date,
       ln.end_date,
       ln.opinion_count,
       COUNT(lo.id) FILTER (WHERE lo.agreement = 'AGREE')    AS agree_count,
       COUNT(lo.id) FILTER (WHERE lo.agreement = 'DISAGREE') AS disagree_count,
       COUNT(lo.id) FILTER (WHERE lo.agreement = 'PRIVATE')  AS private_count,
       p.name                                                AS main_proposer_name,
       pt.unit                                               AS proposer_unit,
       pr.name                                               AS proposer_party

FROM legislative_notices ln
         JOIN bills b ON ln.bill_id = b.id
         LEFT JOIN legislative_opinions lo ON lo.notice_id = ln.id
         LEFT JOIN bill_politician_relations bpr ON bpr.bill_id = b.id AND bpr.role = 'MAIN'
         LEFT JOIN politicians p ON p.id = bpr.politician_id
         LEFT JOIN politician_terms pt ON pt.politician_id = p.id AND pt.unit = b.age
         LEFT JOIN parties pr ON pr.id = pt.party_id

GROUP BY ln.id, ln.opinion_url, ln.id, ln.start_date, ln.end_date,
         ln.opinion_count, ln.start_date, ln.end_date, ln.opinion_count,
         b.bill_id, b.title, b.age, b.propose_date,
         b.bill_id, b.title, b.age, b.detail_link,
         p.name, pt.unit, pt.unit, pr.name, pr.name;
CREATE UNIQUE INDEX uq_popular_legislative_notices_mat_id
    ON legislative_notices_mat (notice_id);

-- 입법예고 상세
CREATE MATERIALIZED VIEW legislative_notice_detail_mat AS
SELECT ln.id                                                 AS notice_id,
       b.id                                                  AS bill_id,
       b.bill_no,
       b.title                                               AS bill_title,
       b.summary,
       b.propose_date,
       b.detail_link,
       b.age,
       ln.opinion_url,
       ln.start_date,
       ln.end_date,
       ln.opinion_count,
       COUNT(lo.id) FILTER (WHERE lo.agreement = 'AGREE')    AS agree_count,
       COUNT(lo.id) FILTER (WHERE lo.agreement = 'DISAGREE') AS disagree_count,
       COUNT(lo.id) FILTER (WHERE lo.agreement = 'PRIVATE')  AS private_count,
       c.name                                                AS committee_name

FROM legislative_notices ln
         JOIN bills b ON ln.bill_id = b.id
         LEFT JOIN legislative_opinions lo ON lo.notice_id = ln.id
         LEFT JOIN committees c ON c.id = b.committee_id

GROUP BY ln.id, ln.opinion_url, ln.start_date, ln.end_date, ln.opinion_count,
         b.bill_no, b.id, b.title, b.summary, b.age, b.propose_date, b.detail_link,
         c.name;
CREATE UNIQUE INDEX uq_legislative_notice_detail_mat_id
    ON legislative_notice_detail_mat (notice_id);

-- 국회의원 리스트
CREATE MATERIALIZED VIEW politicians_mat AS
SELECT p.id                                                                           AS politician_id,
       p.mona_cd,
       p.name                                                                         AS name,
       parties.name                                                                   AS party_name,
       p.profile_image                                                                AS profile_image,
       pt.constituency                                                                AS district,
       pt.job_title                                                                   AS position,
       pt.unit                                                                        AS term,
       c.name                                                                         AS committee_name,
       COUNT(DISTINCT bpr.bill_id)                                                    AS total_bills,
       COUNT(DISTINCT CASE
                          WHEN b.propose_date >= date_trunc('year', CURRENT_DATE)
                              AND b.propose_date < date_trunc('year', CURRENT_DATE) + INTERVAL '1 year'
                              THEN b.id
                          ELSE NULL
           END)                                                                       AS recent_bills,
       COUNT(DISTINCT CASE WHEN b.result IN ('원안가결', '수정가결') THEN b.id ELSE NULL END) AS passed_bills

FROM politicians p
         LEFT JOIN (SELECT DISTINCT ON (politician_id) *
                    FROM politician_terms
                    ORDER BY politician_id, unit DESC) pt ON pt.politician_id = p.id
         LEFT JOIN committees c ON pt.committee_id = c.id
         LEFT JOIN parties ON pt.party_id = parties.id
         LEFT JOIN bill_politician_relations bpr ON bpr.politician_id = p.id
         LEFT JOIN bills b ON b.id = bpr.bill_id

GROUP BY p.id, p.name, p.profile_image, p.mona_cd,
         pt.constituency, pt.job_title, pt.unit,
         parties.name,
         c.name;
CREATE UNIQUE INDEX idx_politicians_mat_id ON politicians_mat (politician_id);

-- 국회의원 상세
CREATE MATERIALIZED VIEW politician_detail_mat AS
SELECT p.id                                                                           AS politician_id,
       p.mona_cd,
       p.name,
       p.eng_name,
       p.hanja_name,
       p.birth_date,
       p.gender                                                                       AS gender,
       pt.unit,
       pt.constituency                                                                AS district,
       pt.job_title                                                                   AS position,
       parties.name                                                                   AS party,
       p.profile_image,
       pc.phone,
       pc.email,
       pc.homepage,
       pc.office_room,
       pc.staff,
       pc.secretary,
       pc.secretary2,
       sns.twitter_url,
       sns.facebook_url,
       sns.youtube_url,
       COUNT(DISTINCT bpr.bill_id)                                                    AS total_bills,
       COUNT(DISTINCT CASE
                          WHEN b.propose_date >= date_trunc('year', CURRENT_DATE)
                              THEN b.id
           END
       )                                                                              AS recent_bills,
       COUNT(DISTINCT CASE WHEN b.result IN ('원안가결', '수정가결') THEN b.id ELSE NULL END) AS passed_bills
FROM politicians p
         LEFT JOIN politician_terms pt ON pt.politician_id = p.id
         LEFT JOIN parties ON pt.party_id = parties.id
         LEFT JOIN politician_contacts pc ON pc.politician_id = p.id
         LEFT JOIN politician_sns sns ON sns.politician_id = p.id
         LEFT JOIN bill_politician_relations bpr ON bpr.politician_id = p.id
         LEFT JOIN bills b ON b.id = bpr.bill_id
WHERE pt.unit = (SELECT MAX(unit) FROM politician_terms)
GROUP BY p.id, p.mona_cd, p.name, p.birth_date, p.gender, p.profile_image,
         pt.unit, pt.constituency, pt.job_title,
         parties.name,
         pc.phone, pc.email, pc.homepage, pc.office_room,
         pc.staff, pc.secretary, pc.secretary2,
         sns.twitter_url, sns.facebook_url, sns.youtube_url;
CREATE UNIQUE INDEX idx_politician_detail_mat_id ON politician_detail_mat (politician_id);


-- 갱신
REFRESH MATERIALIZED VIEW CONCURRENTLY legislative_notices_mat;
REFRESH MATERIALIZED VIEW CONCURRENTLY legislative_notice_detail_mat;
REFRESH MATERIALIZED VIEW CONCURRENTLY politicians_mat;
REFRESH MATERIALIZED VIEW CONCURRENTLY politician_detail_mat;


