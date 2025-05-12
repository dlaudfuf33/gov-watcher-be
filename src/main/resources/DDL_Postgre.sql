-- ===============================
-- ✅ ENUM TYPE 정의
-- ===============================
CREATE TYPE gender_enum AS ENUM ('MALE', 'FEMALE');
CREATE TYPE opinion_enum AS ENUM ('AGREE', 'DISAGREE', 'PRIVATE');
CREATE TYPE proposer_role_enum AS ENUM ('MAIN', 'SUB');

-- ===============================
-- 🏛️ 정당 테이블 (마스터 관리)
-- ===============================
CREATE TABLE parties
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(100) UNIQUE NOT NULL,
    color       VARCHAR(20), -- HEX 색상 코드 (#FF0000)
    logo_url    TEXT,        -- 로고 이미지 URL
    description TEXT         -- 설명
);
CREATE INDEX idx_party_id ON parties (id);

-- ===============================
-- 🏢 상임위 테이블 (마스터 관리)
-- ===============================
CREATE TABLE committees
(
    id          SERIAL PRIMARY KEY,
    name        TEXT UNIQUE NOT NULL,
    color       VARCHAR(20),
    logo_url    TEXT,
    description TEXT
);


-- ===============================
-- 🎩 국회의원 기본 인적사항 테이블
-- ===============================
CREATE TABLE politicians
(
    id            SERIAL PRIMARY KEY,
    mona_cd       VARCHAR(20) UNIQUE NOT NULL,
    name          VARCHAR(100),
    hanja_name    VARCHAR(100),
    eng_name      VARCHAR(100),
    birth_date    DATE,
    gender        gender_enum,
    profile_image TEXT      DEFAULT '',
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE UNIQUE INDEX idx_politicians_id ON politicians (id);
CREATE INDEX idx_politician_lookup ON politicians (name, hanja_name);
CREATE INDEX idx_politicians_name ON politicians (name);

-- ===============================
-- 🗳️ 의원의 대수별 정치 이력
-- ===============================
CREATE TABLE politician_terms
(
    id            SERIAL PRIMARY KEY,
    politician_id INT NOT NULL REFERENCES politicians (id) ON DELETE CASCADE,
    unit          INT NOT NULL,                -- 대수 (예: 21)
    party_id      INT REFERENCES parties (id), -- 정당 ID
    constituency  VARCHAR(100),
    reelected     VARCHAR(10),
    job_title     VARCHAR(100),
    committee_id  INT REFERENCES committees (id),
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (politician_id, unit)
);
CREATE INDEX idx_term_lookup ON politician_terms (politician_id, party_id);
CREATE INDEX idx_politician_terms_party_id ON politician_terms (party_id);
CREATE INDEX idx_politician_terms_politician_id ON politician_terms (politician_id);
CREATE INDEX idx_politician_terms_unit ON politician_terms (unit);
-- ===============================
-- ☎️ 의원 연락처
-- ===============================
CREATE TABLE politician_contacts
(
    politician_id INT PRIMARY KEY REFERENCES politicians (id) ON DELETE CASCADE,
    phone         VARCHAR(50),
    email         VARCHAR(100),
    homepage      TEXT,
    office_room   VARCHAR(100),
    staff         VARCHAR(100),
    secretary     VARCHAR(100),
    secretary2    VARCHAR(100),
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ===============================
-- 🌐 SNS 정보
-- ===============================
CREATE TABLE politician_sns
(
    politician_id INT PRIMARY KEY REFERENCES politicians (id) ON DELETE CASCADE,
    twitter_url   TEXT      DEFAULT '' NOT NULL,
    facebook_url  TEXT      DEFAULT '' NOT NULL,
    youtube_url   TEXT      DEFAULT '' NOT NULL,
    blog_url      TEXT      DEFAULT '' NOT NULL,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ===============================
-- 🧾 의원 약력
-- ===============================
CREATE TABLE politician_careers
(
    politician_id INT PRIMARY KEY REFERENCES politicians (id) ON DELETE CASCADE,
    career        TEXT      DEFAULT '' NOT NULL,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ===============================
-- 📜 발의 법률안 기본 정보
-- ===============================
CREATE TABLE bills
(
    id                 SERIAL PRIMARY KEY,
    bill_id            VARCHAR(100) UNIQUE NOT NULL,
    bill_no            VARCHAR(100),
    title              VARCHAR(500),
    committee_id       INT REFERENCES committees (id),
    age                INT,
    propose_date       DATE,
    law_proc_date      DATE,
    law_present_date   DATE,
    law_submit_date    DATE,
    cmt_proc_date      DATE,
    cmt_present_date   DATE,
    committee_date     DATE,
    proc_date          DATE,
    result             VARCHAR(255),
    law_proc_result_cd VARCHAR(100),
    cmt_proc_result_cd VARCHAR(100),
    detail_link        TEXT,
    summary            TEXT,
    current_step       TEXT,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_bill_no ON bills (bill_no);
CREATE INDEX idx_bills_propose_date ON bills (propose_date);
CREATE INDEX idx_bills_result ON bills (result);

-- ===============================
-- 📊 법안의 심사진행단계 히스토리
-- ===============================
CREATE TABLE bill_status_flows
(
    id         SERIAL PRIMARY KEY,
    bill_id    INT NOT NULL REFERENCES bills (id) ON DELETE CASCADE,
    step_order INT,
    step_name  VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (bill_id, step_order)
);
CREATE INDEX idx_bill_step ON bill_status_flows (bill_id, step_order);
CREATE INDEX idx_bill_status_step_name ON bill_status_flows (bill_id, step_name);

-- ===============================
-- 👥 법안-의원 관계 테이블 (제안자)
-- ===============================
CREATE TABLE bill_politician_relations
(
    id            SERIAL PRIMARY KEY,
    bill_id       INT NOT NULL REFERENCES bills (id) ON DELETE CASCADE,
    politician_id INT NOT NULL REFERENCES politicians (id) ON DELETE CASCADE,
    role          proposer_role_enum,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_bill_politician_role UNIQUE (bill_id, politician_id, role)
);
CREATE INDEX idx_bill_politician ON bill_politician_relations (bill_id, politician_id);
CREATE INDEX idx_bpr_politician_id ON bill_politician_relations (politician_id);
CREATE INDEX idx_bpr_bill_id ON bill_politician_relations (bill_id);

-- ===============================
-- ✍️ 입법예고 테이블
-- ===============================
CREATE TABLE legislative_notices
(
    id            SERIAL PRIMARY KEY,
    bill_id       INT UNIQUE           NOT NULL REFERENCES bills (id) ON DELETE CASCADE,
    start_date    DATE,
    end_date      DATE,
    opinion_url   TEXT      DEFAULT '' NOT NULL,
    opinion_count INT       DEFAULT 0,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ===============================
-- 💬 입법예고 의견 테이블
-- ===============================
CREATE TABLE legislative_opinions
(
    id         SERIAL PRIMARY KEY,
    notice_id  INT                  NOT NULL REFERENCES legislative_notices (id) ON DELETE CASCADE,
    opn_no     BIGINT,
    subject    TEXT      DEFAULT '' NOT NULL,
    content    TEXT      DEFAULT '',
    author     VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    agreement  opinion_enum,
    UNIQUE (notice_id, opn_no)
);
CREATE INDEX idx_legislative_opinions_agreement ON legislative_opinions (notice_id, agreement);

-- ===============================
-- 📈 국회 통계 테이블
-- ===============================
CREATE TABLE parliament_stats
(
    session                      INT PRIMARY KEY,
    total_bills                  BIGINT NOT NULL,
    passed_bills                 BIGINT NOT NULL,
    total_comments               BIGINT NOT NULL,
    bill_change_period           BIGINT           DEFAULT 0,
    bill_change_period_ratio     DOUBLE PRECISION DEFAULT 0,
    bill_change_recent           BIGINT           DEFAULT 0,
    bill_change_recent_ratio     DOUBLE PRECISION DEFAULT 0,
    pass_change_period           BIGINT           DEFAULT 0,
    pass_change_period_ratio     DOUBLE PRECISION DEFAULT 0,
    pass_change_recent           BIGINT           DEFAULT 0,
    pass_change_recent_ratio     DOUBLE PRECISION DEFAULT 0,
    comments_change_period       BIGINT           DEFAULT 0,
    comments_change_period_ratio DOUBLE PRECISION DEFAULT 0,
    comments_change_recent       BIGINT           DEFAULT 0,
    comments_change_recent_ratio DOUBLE PRECISION DEFAULT 0,
    updated_at                   TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

-- ===============================
-- 📅 국회 통계 스냅샷 테이블
-- ===============================
CREATE TABLE parliament_stat_history
(
    id             SERIAL PRIMARY KEY,
    session        INT    NOT NULL,
    snapshot_date  DATE   NOT NULL,
    total_bills    BIGINT NOT NULL,
    passed_bills   BIGINT NOT NULL,
    total_comments BIGINT NOT NULL,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (session, snapshot_date)
);

-- ===============================
-- 🙋‍♂️ 건의사항 테이블
-- ===============================
CREATE TABLE suggestions
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(100),
    email        VARCHAR(100),
    type         VARCHAR(20) NOT NULL CHECK (type IN ('FEATURE', 'BUG', 'IMPROVEMENT', 'OTHER')),
    category     VARCHAR(50) NOT NULL,
    content      TEXT        NOT NULL,
    is_anonymous BOOLEAN              DEFAULT FALSE,

    status       VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'REVIEWING', 'COMPLETED', 'REJECTED')),
    votes        INTEGER              DEFAULT 0,
    created_at   TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP            DEFAULT CURRENT_TIMESTAMP
);

-- ===============================
-- 🗳️ 건의사항 추천 중복방지 테이블
-- ===============================
CREATE TABLE suggestion_votes
(
    id            BIGSERIAL PRIMARY KEY,
    suggestion_id BIGINT       NOT NULL,
    visitor_id    VARCHAR(128) NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    UNIQUE (suggestion_id, visitor_id),

    CONSTRAINT fk_suggestion_vote
        FOREIGN KEY (suggestion_id)
            REFERENCES suggestions (id)
            ON DELETE CASCADE
);


-- 20대 국회
INSERT INTO parliament_stats (session, total_bills, passed_bills, total_comments,
                              bill_change_period, bill_change_recent,
                              pass_change_period, pass_change_recent,
                              comments_change_period, comments_change_recent,
                              updated_at)
VALUES (20,
        21594,
        3194,
        41396200,
        0, 0,
        0, 0,
        0, 0,
        NOW());

-- 21대 국회
INSERT INTO parliament_stats (session, total_bills, passed_bills, total_comments,
                              bill_change_period, bill_change_recent,
                              pass_change_period, pass_change_recent,
                              comments_change_period, comments_change_recent,
                              updated_at)
VALUES (21,
        23655,
        2974,
        6524337,
        2061, 0,
        -220, 0,
        -34871863, 0,
        NOW());

-- ===============================
-- 🔄 updated_at 자동 갱신 트리거 함수
-- ===============================
CREATE
    OR REPLACE FUNCTION update_timestamp()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.updated_at
        = NOW();
    RETURN NEW;
END;
$$
    LANGUAGE plpgsql;
