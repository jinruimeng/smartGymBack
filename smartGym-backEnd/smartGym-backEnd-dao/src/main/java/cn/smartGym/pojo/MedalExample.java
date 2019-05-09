package cn.smartGym.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MedalExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public MedalExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andGameIsNull() {
            addCriterion("game is null");
            return (Criteria) this;
        }

        public Criteria andGameIsNotNull() {
            addCriterion("game is not null");
            return (Criteria) this;
        }

        public Criteria andGameEqualTo(String value) {
            addCriterion("game =", value, "game");
            return (Criteria) this;
        }

        public Criteria andGameNotEqualTo(String value) {
            addCriterion("game <>", value, "game");
            return (Criteria) this;
        }

        public Criteria andGameGreaterThan(String value) {
            addCriterion("game >", value, "game");
            return (Criteria) this;
        }

        public Criteria andGameGreaterThanOrEqualTo(String value) {
            addCriterion("game >=", value, "game");
            return (Criteria) this;
        }

        public Criteria andGameLessThan(String value) {
            addCriterion("game <", value, "game");
            return (Criteria) this;
        }

        public Criteria andGameLessThanOrEqualTo(String value) {
            addCriterion("game <=", value, "game");
            return (Criteria) this;
        }

        public Criteria andGameLike(String value) {
            addCriterion("game like", value, "game");
            return (Criteria) this;
        }

        public Criteria andGameNotLike(String value) {
            addCriterion("game not like", value, "game");
            return (Criteria) this;
        }

        public Criteria andGameIn(List<String> values) {
            addCriterion("game in", values, "game");
            return (Criteria) this;
        }

        public Criteria andGameNotIn(List<String> values) {
            addCriterion("game not in", values, "game");
            return (Criteria) this;
        }

        public Criteria andGameBetween(String value1, String value2) {
            addCriterion("game between", value1, value2, "game");
            return (Criteria) this;
        }

        public Criteria andGameNotBetween(String value1, String value2) {
            addCriterion("game not between", value1, value2, "game");
            return (Criteria) this;
        }

        public Criteria andCollegeIsNull() {
            addCriterion("college is null");
            return (Criteria) this;
        }

        public Criteria andCollegeIsNotNull() {
            addCriterion("college is not null");
            return (Criteria) this;
        }

        public Criteria andCollegeEqualTo(Integer value) {
            addCriterion("college =", value, "college");
            return (Criteria) this;
        }

        public Criteria andCollegeNotEqualTo(Integer value) {
            addCriterion("college <>", value, "college");
            return (Criteria) this;
        }

        public Criteria andCollegeGreaterThan(Integer value) {
            addCriterion("college >", value, "college");
            return (Criteria) this;
        }

        public Criteria andCollegeGreaterThanOrEqualTo(Integer value) {
            addCriterion("college >=", value, "college");
            return (Criteria) this;
        }

        public Criteria andCollegeLessThan(Integer value) {
            addCriterion("college <", value, "college");
            return (Criteria) this;
        }

        public Criteria andCollegeLessThanOrEqualTo(Integer value) {
            addCriterion("college <=", value, "college");
            return (Criteria) this;
        }

        public Criteria andCollegeIn(List<Integer> values) {
            addCriterion("college in", values, "college");
            return (Criteria) this;
        }

        public Criteria andCollegeNotIn(List<Integer> values) {
            addCriterion("college not in", values, "college");
            return (Criteria) this;
        }

        public Criteria andCollegeBetween(Integer value1, Integer value2) {
            addCriterion("college between", value1, value2, "college");
            return (Criteria) this;
        }

        public Criteria andCollegeNotBetween(Integer value1, Integer value2) {
            addCriterion("college not between", value1, value2, "college");
            return (Criteria) this;
        }

        public Criteria andFirstIsNull() {
            addCriterion("first is null");
            return (Criteria) this;
        }

        public Criteria andFirstIsNotNull() {
            addCriterion("first is not null");
            return (Criteria) this;
        }

        public Criteria andFirstEqualTo(Integer value) {
            addCriterion("first =", value, "first");
            return (Criteria) this;
        }

        public Criteria andFirstNotEqualTo(Integer value) {
            addCriterion("first <>", value, "first");
            return (Criteria) this;
        }

        public Criteria andFirstGreaterThan(Integer value) {
            addCriterion("first >", value, "first");
            return (Criteria) this;
        }

        public Criteria andFirstGreaterThanOrEqualTo(Integer value) {
            addCriterion("first >=", value, "first");
            return (Criteria) this;
        }

        public Criteria andFirstLessThan(Integer value) {
            addCriterion("first <", value, "first");
            return (Criteria) this;
        }

        public Criteria andFirstLessThanOrEqualTo(Integer value) {
            addCriterion("first <=", value, "first");
            return (Criteria) this;
        }

        public Criteria andFirstIn(List<Integer> values) {
            addCriterion("first in", values, "first");
            return (Criteria) this;
        }

        public Criteria andFirstNotIn(List<Integer> values) {
            addCriterion("first not in", values, "first");
            return (Criteria) this;
        }

        public Criteria andFirstBetween(Integer value1, Integer value2) {
            addCriterion("first between", value1, value2, "first");
            return (Criteria) this;
        }

        public Criteria andFirstNotBetween(Integer value1, Integer value2) {
            addCriterion("first not between", value1, value2, "first");
            return (Criteria) this;
        }

        public Criteria andSecondIsNull() {
            addCriterion("second is null");
            return (Criteria) this;
        }

        public Criteria andSecondIsNotNull() {
            addCriterion("second is not null");
            return (Criteria) this;
        }

        public Criteria andSecondEqualTo(Integer value) {
            addCriterion("second =", value, "second");
            return (Criteria) this;
        }

        public Criteria andSecondNotEqualTo(Integer value) {
            addCriterion("second <>", value, "second");
            return (Criteria) this;
        }

        public Criteria andSecondGreaterThan(Integer value) {
            addCriterion("second >", value, "second");
            return (Criteria) this;
        }

        public Criteria andSecondGreaterThanOrEqualTo(Integer value) {
            addCriterion("second >=", value, "second");
            return (Criteria) this;
        }

        public Criteria andSecondLessThan(Integer value) {
            addCriterion("second <", value, "second");
            return (Criteria) this;
        }

        public Criteria andSecondLessThanOrEqualTo(Integer value) {
            addCriterion("second <=", value, "second");
            return (Criteria) this;
        }

        public Criteria andSecondIn(List<Integer> values) {
            addCriterion("second in", values, "second");
            return (Criteria) this;
        }

        public Criteria andSecondNotIn(List<Integer> values) {
            addCriterion("second not in", values, "second");
            return (Criteria) this;
        }

        public Criteria andSecondBetween(Integer value1, Integer value2) {
            addCriterion("second between", value1, value2, "second");
            return (Criteria) this;
        }

        public Criteria andSecondNotBetween(Integer value1, Integer value2) {
            addCriterion("second not between", value1, value2, "second");
            return (Criteria) this;
        }

        public Criteria andThirdIsNull() {
            addCriterion("third is null");
            return (Criteria) this;
        }

        public Criteria andThirdIsNotNull() {
            addCriterion("third is not null");
            return (Criteria) this;
        }

        public Criteria andThirdEqualTo(Integer value) {
            addCriterion("third =", value, "third");
            return (Criteria) this;
        }

        public Criteria andThirdNotEqualTo(Integer value) {
            addCriterion("third <>", value, "third");
            return (Criteria) this;
        }

        public Criteria andThirdGreaterThan(Integer value) {
            addCriterion("third >", value, "third");
            return (Criteria) this;
        }

        public Criteria andThirdGreaterThanOrEqualTo(Integer value) {
            addCriterion("third >=", value, "third");
            return (Criteria) this;
        }

        public Criteria andThirdLessThan(Integer value) {
            addCriterion("third <", value, "third");
            return (Criteria) this;
        }

        public Criteria andThirdLessThanOrEqualTo(Integer value) {
            addCriterion("third <=", value, "third");
            return (Criteria) this;
        }

        public Criteria andThirdIn(List<Integer> values) {
            addCriterion("third in", values, "third");
            return (Criteria) this;
        }

        public Criteria andThirdNotIn(List<Integer> values) {
            addCriterion("third not in", values, "third");
            return (Criteria) this;
        }

        public Criteria andThirdBetween(Integer value1, Integer value2) {
            addCriterion("third between", value1, value2, "third");
            return (Criteria) this;
        }

        public Criteria andThirdNotBetween(Integer value1, Integer value2) {
            addCriterion("third not between", value1, value2, "third");
            return (Criteria) this;
        }

        public Criteria andFourthIsNull() {
            addCriterion("fourth is null");
            return (Criteria) this;
        }

        public Criteria andFourthIsNotNull() {
            addCriterion("fourth is not null");
            return (Criteria) this;
        }

        public Criteria andFourthEqualTo(Integer value) {
            addCriterion("fourth =", value, "fourth");
            return (Criteria) this;
        }

        public Criteria andFourthNotEqualTo(Integer value) {
            addCriterion("fourth <>", value, "fourth");
            return (Criteria) this;
        }

        public Criteria andFourthGreaterThan(Integer value) {
            addCriterion("fourth >", value, "fourth");
            return (Criteria) this;
        }

        public Criteria andFourthGreaterThanOrEqualTo(Integer value) {
            addCriterion("fourth >=", value, "fourth");
            return (Criteria) this;
        }

        public Criteria andFourthLessThan(Integer value) {
            addCriterion("fourth <", value, "fourth");
            return (Criteria) this;
        }

        public Criteria andFourthLessThanOrEqualTo(Integer value) {
            addCriterion("fourth <=", value, "fourth");
            return (Criteria) this;
        }

        public Criteria andFourthIn(List<Integer> values) {
            addCriterion("fourth in", values, "fourth");
            return (Criteria) this;
        }

        public Criteria andFourthNotIn(List<Integer> values) {
            addCriterion("fourth not in", values, "fourth");
            return (Criteria) this;
        }

        public Criteria andFourthBetween(Integer value1, Integer value2) {
            addCriterion("fourth between", value1, value2, "fourth");
            return (Criteria) this;
        }

        public Criteria andFourthNotBetween(Integer value1, Integer value2) {
            addCriterion("fourth not between", value1, value2, "fourth");
            return (Criteria) this;
        }

        public Criteria andFifthIsNull() {
            addCriterion("fifth is null");
            return (Criteria) this;
        }

        public Criteria andFifthIsNotNull() {
            addCriterion("fifth is not null");
            return (Criteria) this;
        }

        public Criteria andFifthEqualTo(Integer value) {
            addCriterion("fifth =", value, "fifth");
            return (Criteria) this;
        }

        public Criteria andFifthNotEqualTo(Integer value) {
            addCriterion("fifth <>", value, "fifth");
            return (Criteria) this;
        }

        public Criteria andFifthGreaterThan(Integer value) {
            addCriterion("fifth >", value, "fifth");
            return (Criteria) this;
        }

        public Criteria andFifthGreaterThanOrEqualTo(Integer value) {
            addCriterion("fifth >=", value, "fifth");
            return (Criteria) this;
        }

        public Criteria andFifthLessThan(Integer value) {
            addCriterion("fifth <", value, "fifth");
            return (Criteria) this;
        }

        public Criteria andFifthLessThanOrEqualTo(Integer value) {
            addCriterion("fifth <=", value, "fifth");
            return (Criteria) this;
        }

        public Criteria andFifthIn(List<Integer> values) {
            addCriterion("fifth in", values, "fifth");
            return (Criteria) this;
        }

        public Criteria andFifthNotIn(List<Integer> values) {
            addCriterion("fifth not in", values, "fifth");
            return (Criteria) this;
        }

        public Criteria andFifthBetween(Integer value1, Integer value2) {
            addCriterion("fifth between", value1, value2, "fifth");
            return (Criteria) this;
        }

        public Criteria andFifthNotBetween(Integer value1, Integer value2) {
            addCriterion("fifth not between", value1, value2, "fifth");
            return (Criteria) this;
        }

        public Criteria andSixthIsNull() {
            addCriterion("sixth is null");
            return (Criteria) this;
        }

        public Criteria andSixthIsNotNull() {
            addCriterion("sixth is not null");
            return (Criteria) this;
        }

        public Criteria andSixthEqualTo(Integer value) {
            addCriterion("sixth =", value, "sixth");
            return (Criteria) this;
        }

        public Criteria andSixthNotEqualTo(Integer value) {
            addCriterion("sixth <>", value, "sixth");
            return (Criteria) this;
        }

        public Criteria andSixthGreaterThan(Integer value) {
            addCriterion("sixth >", value, "sixth");
            return (Criteria) this;
        }

        public Criteria andSixthGreaterThanOrEqualTo(Integer value) {
            addCriterion("sixth >=", value, "sixth");
            return (Criteria) this;
        }

        public Criteria andSixthLessThan(Integer value) {
            addCriterion("sixth <", value, "sixth");
            return (Criteria) this;
        }

        public Criteria andSixthLessThanOrEqualTo(Integer value) {
            addCriterion("sixth <=", value, "sixth");
            return (Criteria) this;
        }

        public Criteria andSixthIn(List<Integer> values) {
            addCriterion("sixth in", values, "sixth");
            return (Criteria) this;
        }

        public Criteria andSixthNotIn(List<Integer> values) {
            addCriterion("sixth not in", values, "sixth");
            return (Criteria) this;
        }

        public Criteria andSixthBetween(Integer value1, Integer value2) {
            addCriterion("sixth between", value1, value2, "sixth");
            return (Criteria) this;
        }

        public Criteria andSixthNotBetween(Integer value1, Integer value2) {
            addCriterion("sixth not between", value1, value2, "sixth");
            return (Criteria) this;
        }

        public Criteria andSeventhIsNull() {
            addCriterion("seventh is null");
            return (Criteria) this;
        }

        public Criteria andSeventhIsNotNull() {
            addCriterion("seventh is not null");
            return (Criteria) this;
        }

        public Criteria andSeventhEqualTo(Integer value) {
            addCriterion("seventh =", value, "seventh");
            return (Criteria) this;
        }

        public Criteria andSeventhNotEqualTo(Integer value) {
            addCriterion("seventh <>", value, "seventh");
            return (Criteria) this;
        }

        public Criteria andSeventhGreaterThan(Integer value) {
            addCriterion("seventh >", value, "seventh");
            return (Criteria) this;
        }

        public Criteria andSeventhGreaterThanOrEqualTo(Integer value) {
            addCriterion("seventh >=", value, "seventh");
            return (Criteria) this;
        }

        public Criteria andSeventhLessThan(Integer value) {
            addCriterion("seventh <", value, "seventh");
            return (Criteria) this;
        }

        public Criteria andSeventhLessThanOrEqualTo(Integer value) {
            addCriterion("seventh <=", value, "seventh");
            return (Criteria) this;
        }

        public Criteria andSeventhIn(List<Integer> values) {
            addCriterion("seventh in", values, "seventh");
            return (Criteria) this;
        }

        public Criteria andSeventhNotIn(List<Integer> values) {
            addCriterion("seventh not in", values, "seventh");
            return (Criteria) this;
        }

        public Criteria andSeventhBetween(Integer value1, Integer value2) {
            addCriterion("seventh between", value1, value2, "seventh");
            return (Criteria) this;
        }

        public Criteria andSeventhNotBetween(Integer value1, Integer value2) {
            addCriterion("seventh not between", value1, value2, "seventh");
            return (Criteria) this;
        }

        public Criteria andEighthIsNull() {
            addCriterion("eighth is null");
            return (Criteria) this;
        }

        public Criteria andEighthIsNotNull() {
            addCriterion("eighth is not null");
            return (Criteria) this;
        }

        public Criteria andEighthEqualTo(Integer value) {
            addCriterion("eighth =", value, "eighth");
            return (Criteria) this;
        }

        public Criteria andEighthNotEqualTo(Integer value) {
            addCriterion("eighth <>", value, "eighth");
            return (Criteria) this;
        }

        public Criteria andEighthGreaterThan(Integer value) {
            addCriterion("eighth >", value, "eighth");
            return (Criteria) this;
        }

        public Criteria andEighthGreaterThanOrEqualTo(Integer value) {
            addCriterion("eighth >=", value, "eighth");
            return (Criteria) this;
        }

        public Criteria andEighthLessThan(Integer value) {
            addCriterion("eighth <", value, "eighth");
            return (Criteria) this;
        }

        public Criteria andEighthLessThanOrEqualTo(Integer value) {
            addCriterion("eighth <=", value, "eighth");
            return (Criteria) this;
        }

        public Criteria andEighthIn(List<Integer> values) {
            addCriterion("eighth in", values, "eighth");
            return (Criteria) this;
        }

        public Criteria andEighthNotIn(List<Integer> values) {
            addCriterion("eighth not in", values, "eighth");
            return (Criteria) this;
        }

        public Criteria andEighthBetween(Integer value1, Integer value2) {
            addCriterion("eighth between", value1, value2, "eighth");
            return (Criteria) this;
        }

        public Criteria andEighthNotBetween(Integer value1, Integer value2) {
            addCriterion("eighth not between", value1, value2, "eighth");
            return (Criteria) this;
        }

        public Criteria andScoreIsNull() {
            addCriterion("score is null");
            return (Criteria) this;
        }

        public Criteria andScoreIsNotNull() {
            addCriterion("score is not null");
            return (Criteria) this;
        }

        public Criteria andScoreEqualTo(Integer value) {
            addCriterion("score =", value, "score");
            return (Criteria) this;
        }

        public Criteria andScoreNotEqualTo(Integer value) {
            addCriterion("score <>", value, "score");
            return (Criteria) this;
        }

        public Criteria andScoreGreaterThan(Integer value) {
            addCriterion("score >", value, "score");
            return (Criteria) this;
        }

        public Criteria andScoreGreaterThanOrEqualTo(Integer value) {
            addCriterion("score >=", value, "score");
            return (Criteria) this;
        }

        public Criteria andScoreLessThan(Integer value) {
            addCriterion("score <", value, "score");
            return (Criteria) this;
        }

        public Criteria andScoreLessThanOrEqualTo(Integer value) {
            addCriterion("score <=", value, "score");
            return (Criteria) this;
        }

        public Criteria andScoreIn(List<Integer> values) {
            addCriterion("score in", values, "score");
            return (Criteria) this;
        }

        public Criteria andScoreNotIn(List<Integer> values) {
            addCriterion("score not in", values, "score");
            return (Criteria) this;
        }

        public Criteria andScoreBetween(Integer value1, Integer value2) {
            addCriterion("score between", value1, value2, "score");
            return (Criteria) this;
        }

        public Criteria andScoreNotBetween(Integer value1, Integer value2) {
            addCriterion("score not between", value1, value2, "score");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andCreatedIsNull() {
            addCriterion("created is null");
            return (Criteria) this;
        }

        public Criteria andCreatedIsNotNull() {
            addCriterion("created is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedEqualTo(Date value) {
            addCriterion("created =", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedNotEqualTo(Date value) {
            addCriterion("created <>", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedGreaterThan(Date value) {
            addCriterion("created >", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedGreaterThanOrEqualTo(Date value) {
            addCriterion("created >=", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedLessThan(Date value) {
            addCriterion("created <", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedLessThanOrEqualTo(Date value) {
            addCriterion("created <=", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedIn(List<Date> values) {
            addCriterion("created in", values, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedNotIn(List<Date> values) {
            addCriterion("created not in", values, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedBetween(Date value1, Date value2) {
            addCriterion("created between", value1, value2, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedNotBetween(Date value1, Date value2) {
            addCriterion("created not between", value1, value2, "created");
            return (Criteria) this;
        }

        public Criteria andUpdatedIsNull() {
            addCriterion("updated is null");
            return (Criteria) this;
        }

        public Criteria andUpdatedIsNotNull() {
            addCriterion("updated is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatedEqualTo(Date value) {
            addCriterion("updated =", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedNotEqualTo(Date value) {
            addCriterion("updated <>", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedGreaterThan(Date value) {
            addCriterion("updated >", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedGreaterThanOrEqualTo(Date value) {
            addCriterion("updated >=", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedLessThan(Date value) {
            addCriterion("updated <", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedLessThanOrEqualTo(Date value) {
            addCriterion("updated <=", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedIn(List<Date> values) {
            addCriterion("updated in", values, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedNotIn(List<Date> values) {
            addCriterion("updated not in", values, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedBetween(Date value1, Date value2) {
            addCriterion("updated between", value1, value2, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedNotBetween(Date value1, Date value2) {
            addCriterion("updated not between", value1, value2, "updated");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}