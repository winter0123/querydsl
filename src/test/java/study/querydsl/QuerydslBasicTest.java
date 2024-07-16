package study.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QMember.*;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {
    @PersistenceContext
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }
    @Test
    public void startJPQL() {
        //member1을 찾아라.
        String qlString =
                "select m from Member m " +
                        "where m.username = :username";
        Member findMember = em.createQuery(qlString, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    /** 기본  Q-TYPE 쿼리 쿼리 **/
    @Test
    public void startQuerydsl() {
        //member1을 찾아라.
        //QMember m = new QMember("m");
        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1"))//파라미터 바인딩 처리
                .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    /** 검색 조건 쿼리 **/
    @Test
    public void search() {
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1")
                        .and(member.age.eq(10)))
                .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");

        // 검색 조건들
//        member.username.eq("member1")         // username = 'member1'
//        member.username.ne("member1")         //username != 'member1'
//        member.username.eq("member1").not()   // username != 'member1'
//        member.username.isNotNull() //이름이 is not null
//        member.age.in(10, 20)     // age in (10,20)
//        member.age.notIn(10, 20)  // age not in (10, 20)
//        member.age.between(10,30) //between 10, 30
//        member.age.goe(30)    // age >= 30
//        member.age.gt(30)     // age > 30
//        member.age.loe(30)    // age <= 30
//        member.age.lt(30)     // age < 30
//        member.username.like("member%")       //like 검색
//        member.username.contains("member")    // like ‘%member%’ 검색
//        member.username.startsWith("member")  //like ‘member%’ 검색
    }

    /** 결과 조회 **/
    @Test
    public void resultFetch(){
//        fetch() : 리스트 조회, 데이터 없으면 빈 리스트 반환
//        fetchOne(): 단 건 조회
//            - 결과가 없으면 : null
//            - 결과가 둘 이상이면 : com.querydsl.core.NonUniqueResultException
//        fetchFirst() : limit(1).fetchOne()
//        fetchResults() : 페이징 정보 포함, total count 쿼리 추가 실행
//        fetchCount() :  count 쿼리로 변경해서 count 수 조회

        //List
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .fetch();
        //단 건
        Member findMember1 = queryFactory
                .selectFrom(member)
                .fetchOne();
        //처음 한 건 조회
        Member findMember2 = queryFactory
                .selectFrom(member)
                .fetchFirst();
        //페이징에서 사용
        QueryResults<Member> results = queryFactory
                .selectFrom(member)
                .fetchResults();
//        results.getTotal(); //총페이지 가져오기
//        List<Member> content = results.getResults(); //내용가져오기

        //count 쿼리로 변경
        long count = queryFactory
                .selectFrom(member)
                .fetchCount();
    }






}
