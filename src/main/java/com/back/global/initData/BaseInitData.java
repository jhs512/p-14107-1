package com.back.global.initData;

import com.back.domain.cash.cashLog.entity.CashLog;
import com.back.domain.cash.wallet.entity.Wallet;
import com.back.domain.cash.wallet.service.WalletService;
import com.back.domain.market.cart.entity.Cart;
import com.back.domain.market.cart.service.CartService;
import com.back.domain.market.order.entity.Order;
import com.back.domain.market.order.service.OrderService;
import com.back.domain.market.product.entity.Product;
import com.back.domain.market.product.service.ProductService;
import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.service.MemberService;
import com.back.domain.payout.payout.service.PayoutService;
import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import com.back.domain.post.postChain.entity.PostChain;
import com.back.domain.post.postChain.service.PostChainService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
@Slf4j
public class BaseInitData {
    private final BaseInitData self;
    private final MemberService memberService;
    private final PostService postService;
    private final PostChainService postChainService;
    private final ProductService productService;
    private final CartService cartService;
    private final WalletService walletService;
    private final OrderService orderService;
    private final PayoutService payoutService;
    private final JobOperator jobOperator;
    private final Job makePayoutJob;

    public BaseInitData(
            @Lazy BaseInitData self,
            MemberService memberService,
            PostService postService,
            PostChainService postChainService,
            ProductService productService,
            CartService cartService,
            WalletService walletService,
            OrderService orderService,
            PayoutService payoutService,
            JobOperator jobOperator,
            Job makePayoutJob
    ) {
        this.self = self;
        this.memberService = memberService;
        this.postService = postService;
        this.postChainService = postChainService;
        this.productService = productService;
        this.cartService = cartService;
        this.walletService = walletService;
        this.orderService = orderService;
        this.payoutService = payoutService;
        this.jobOperator = jobOperator;
        this.makePayoutJob = makePayoutJob;
    }

    @Bean
    public ApplicationRunner baseInitDataRunner() {
        return args -> {
            self.work1();
            self.work2();
            self.work3();
            self.work4();
            self.work5();
            self.work6();
            self.work7();
            self.work8();
            self.work9();
            self.work10();
            self.work11();
            self.work12();
        };
    }

    @Transactional
    public void work1() {
        if (memberService.count() > 0) return;

        Member systemMember = memberService.join("system", "1234", "시스템");
        Member holdingMember = memberService.join("holding", "1234", "홀딩");
        Member adminMember = memberService.join("admin", "1234", "관리자");
        Member user1Member = memberService.join("user1", "1234", "유저1");
        Member user2Member = memberService.join("user2", "1234", "유저2");
        Member user3Member = memberService.join("user3", "1234", "유저3");
    }

    @Transactional
    public void work2() {
        if (postService.count() > 0) return;

        Member user1Member = memberService.findByUsername("user1").get();
        Member user2Member = memberService.findByUsername("user2").get();
        Member user3Member = memberService.findByUsername("user3").get();

        Post post1 = postService.write(user1Member, "제목1", "내용1");
        Post post2 = postService.write(user1Member, "제목2", "내용2");
        Post post3 = postService.write(user1Member, "제목3", "내용3");
        Post post4 = postService.write(user2Member, "제목4", "내용4");
        Post post5 = postService.write(user2Member, "제목5", "내용5");
        Post post6 = postService.write(user3Member, "제목6", "내용6");
    }

    @Transactional
    public void work3() {
        Post post1 = postService.findById(1).get();

        if (post1.hasComments()) return;

        Post post2 = postService.findById(2).get();
        Post post3 = postService.findById(3).get();
        Post post4 = postService.findById(4).get();

        Member user1Member = memberService.findByUsername("user1").get();
        Member user2Member = memberService.findByUsername("user2").get();
        Member user3Member = memberService.findByUsername("user3").get();

        post1.addComment(user1Member, "댓글1");
        post1.addComment(user2Member, "댓글2");
        post1.addComment(user3Member, "댓글3");

        post2.addComment(user2Member, "댓글4");
        post2.addComment(user2Member, "댓글5");

        post3.addComment(user3Member, "댓글6");
        post3.addComment(user3Member, "댓글7");

        post4.addComment(user1Member, "댓글8");
    }

    @Transactional
    public void work4() {
        if (postChainService.count() > 0) return;

        Member user1Member = memberService.findByUsername("user1").get();
        Member user2Member = memberService.findByUsername("user2").get();
        Member user3Member = memberService.findByUsername("user3").get();

        PostChain postChain1 = postChainService.make(user1Member, "글 그룹1");
        PostChain postChain2 = postChainService.make(user2Member, "글 그룹2");
        PostChain postChain3 = postChainService.make(user3Member, "글 그룹3");
    }

    @Transactional
    public void work5() {
        PostChain postChain1 = postChainService.findById(1).get();

        if (!postChain1.isEmpty()) return;

        Post post1 = postService.findById(1).get();
        Post post2 = postService.findById(2).get();
        Post post3 = postService.findById(3).get();

        postChain1.addItem(post1);
        postChain1.addItem(post2);
        postChain1.addItem(post3);


        PostChain postChain2 = postChainService.findById(2).get();

        Post post4 = postService.findById(4).get();
        Post post5 = postService.findById(5).get();

        postChain2.addItem(post4);
        postChain2.addItem(post5);


        PostChain postChain3 = postChainService.findById(3).get();

        Post post6 = postService.findById(6).get();

        postChain3.addItem(post6);
    }

    @Transactional
    public void work6() {
        if (productService.count() > 0) return;

        PostChain postChain1 = postChainService.findById(1).get();
        PostChain postChain2 = postChainService.findById(2).get();
        PostChain postChain3 = postChainService.findById(3).get();

        Product product1 = productService.make(postChain1.getAuthor(), postChain1.getModelTypeCode(), postChain1.getId(), postChain1.getTitle(), postChain1.getTitle(), 3_000, 3_000);
        Product product2 = productService.make(postChain2.getAuthor(), postChain2.getModelTypeCode(), postChain2.getId(), postChain2.getTitle(), postChain2.getTitle(), 2_000, 2_000);
        Product product3 = productService.make(postChain3.getAuthor(), postChain3.getModelTypeCode(), postChain3.getId(), postChain3.getTitle(), postChain3.getTitle(), 1_000, 1_000);
    }


    @Transactional
    public void work7() {
        Member user1Member = memberService.findByUsername("user1").get();

        Cart cart = cartService.findByBuyer(user1Member).get();

        if (!cart.isEmpty()) return;

        Product product1 = productService.findById(1).get();
        Product product2 = productService.findById(2).get();

        cart.addItem(product1);
        cart.addItem(product2);
    }

    @Transactional
    public void work8() {
        Member user1Member = memberService.findByUsername("user1").get();

        Wallet wallet = walletService.findByHolder(user1Member).get();

        if (wallet.hasBalance()) return;

        wallet.credit(150_000, CashLog.EventType.충전__무통장입금);
        wallet.credit(100_000, CashLog.EventType.충전__무통장입금);
        wallet.credit(50_000, CashLog.EventType.충전__무통장입금);
    }

    @Transactional
    public void work9() {
        if (orderService.count() > 0) return;

        Member user1Member = memberService.findByUsername("user1").get();

        Cart cart = cartService.findByBuyer(user1Member).get();

        Order order = orderService.make(cart);
    }

    @Transactional
    public void work10() {
        Order order = orderService.findById(1).get();

        if (order.isPaid()) return;

        orderService.completePayment(order);
    }

    @Transactional
    public void work11() {
        payoutService.findPayoutCandidatesDue(100)
                .forEach(payoutCandidateItem -> {
                    log.debug("payoutCandidateItem.getId() : {}", payoutCandidateItem.getId());
                });
    }

    @SneakyThrows
    public void work12() {
        // 만약 포맷을 명시하고 싶다면 (추천)
        JobParameters jobParameters = new JobParametersBuilder()
                // ISO_LOCAL_DATE = yyyy-MM-dd
                .addString(
                        "runDate",
                        LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
                )
                .toJobParameters();

        JobExecution execution = jobOperator.start(makePayoutJob, jobParameters);
    }
}
