<template>
  <div class="login">
    <div class="main">
      <div class="logoContainer">
        <div class="logo"><img src="~assets/img/logo.png" alt="" /></div>
        <div class="name">中传放心传</div>
      </div>
      <div
        class="mainBox"
        :class="activeName == 'first' ? '' : 'mainBoxRegistered'"
      >
        <el-tabs
          v-model="activeName"
          type="card"
          @tab-click="handleClick"
          stretch
        >
          <el-tab-pane label="登录" name="first">
            <div class="loginInput">
              <el-form ref="form" :model="login" label-width="80px">
                <el-form-item>
                  <el-input
                    v-model="login.mobile"
                    placeholder="请输入手机号码"
                  ></el-input>
                </el-form-item>
                <el-form-item>
                  <el-input
                    v-model="login.password"
                    type="password"
                    placeholder="请输入密码"
                  ></el-input>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="onSubmit">登录</el-button>
                </el-form-item>
              </el-form>
            </div>
          </el-tab-pane>
          <el-tab-pane label="注册" name="second">
            <div class="registeredInput">
              <el-form ref="form" :model="login" label-width="80px">
                <el-form-item>
                  <el-input
                    v-model="registered.mobile"
                    placeholder="请输入手机号码"
                    maxlength="11"
                    show-word-limit
                    onkeyup="value=value.replace(/\D/g,'')"
                  >
                    ></el-input
                  >
                </el-form-item>
                <el-form-item>
                  <el-input
                    v-model="registered.password"
                    @input="checkPassLever"
                    type="password"
                    placeholder="请输入密码"
                    onkeyup="value=value.replace(/[^\a-\z\A-\Z0-9\-\_\.]/g,'')"
                    maxlength="36"
                    show-word-limit
                  ></el-input>
                  <table
                    border="0"
                    align="center"
                    style="width: 200px;margin-left: 0px"
                  >
                    <tr>
                      <td width="60px">
                        <el-progress
                          :percentage="100"
                          :color="tr1"
                          :format="format"
                        ></el-progress>
                      </td>
                      <td width="60px">
                        <el-progress
                          :percentage="100"
                          :color="tr2"
                          :format="format"
                        ></el-progress>
                      </td>
                      <td width="60px">
                        <el-progress
                          :percentage="100"
                          :color="tr3"
                          :format="format"
                        ></el-progress>
                      </td>
                      <td width="20px">
                        <div
                          class="strength"
                          :style="{ color: fontColor }"
                          v-if="
                            registered.password !== '' &&
                              registered.password !== undefined
                          "
                        >
                          {{ strength }}
                        </div>
                      </td>
                    </tr>
                  </table>
                </el-form-item>
                <el-form-item>
                  <el-input
                    v-model="registered.nickname"
                    placeholder="请输入昵称"
                  ></el-input>
                </el-form-item>
                <el-form-item class="codeContainer">
                  <el-input
                    v-model="registered.code"
                    placeholder="请输入验证码"
                  ></el-input>
                  <div class="codeButtonContainer">
                    <el-button
                      size="mini"
                      class="getcode"
                      v-if="!isCountDownShow"
                      @click="getCode"
                      >获取验证码</el-button
                    >
                    <div class="countDown" v-else>{{ countDownSecond }} s</div>
                  </div>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="clickRegistered"
                    >注册</el-button
                  >
                </el-form-item>
              </el-form>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>

<script>
// 倒计时名称
let timer;

export default {
  name: "Login",
  data() {
    const checkPawd = (rule, value, callback) => {
      checkPasswd(value).then((res) => {
        if (res.msg !== "notCheck") {
          this.notCheck = false;
          if (res.msg === "低") {
            this.fontColor = "red";
            this.strength = this.indicator["red"];
          }
          if (res.msg === "中") {
            this.fontColor = "orange";
            this.strength = this.indicator["orange"];
          }
          if (res.msg === "高") {
            this.fontColor = "blue";
            this.strength = this.indicator["blue"];
          }
        } else {
          this.notCheck = true;
        }
        callback();
      });
    };
    return {
      tr1: "red",
      tr2: "#FFFFFF",
      tr3: "#FFFFFF",
      notCheck: true,
      fontColor: "red",
      strength: "",
      indicator: {
        red: "低",
        orange: "中",
        blue: "高",
      },
      login: {
        mobile: "",
        password: "",
      },
      registered: {
        mobile: "",
        password: "",
        code: "",
        nickname: "",
        avatar: null,
      },
      activeName: "first",
      // 倒计时秒数
      countDownSecond: 60,
      // 是否显示秒数
      isCountDownShow: false,
    };
  },
  methods: {
    format(percentage) {
      return percentage === 100 ? "" : `${percentage}%`;
    },
    // 验证面膜等级
    checkPassLever(value) {
      // 红 蓝 绿
      if (value.length == 1) {
        this.tr2 = "#FFFFFF";
        this.tr3 = "#FFFFFF";
      }
      if (value.length == 2) {
        this.tr3 = "#FFFFFF";
      }
      if (value.length > 1) {
        var pd1 = /[._-]/;
        var pd2 = /[a-zA-Z]/;
        var pd3 = /[0-9]/;
        var x1 = pd1.test(value) && pd2.test(value);
        var x2 = pd3.test(value) && pd2.test(value);
        var x3 = pd1.test(value) && pd3.test(value);
        if (x1 || x2 || x3) {
          this.tr2 = "blue";
        } else {
          this.tr2 = "#FFFFFF";
        }
      }
      if (value.length > 2) {
        var zg2 = /^(?![^a-zA-Z]+$)(?!\D+$)(?![._-]+$)/;
        var zg3 = /[._-]/;
        if (zg2.test(value) && zg3.test(value)) {
          this.tr3 = "green";
        } else {
          this.tr3 = "#FFFFFF";
        }
      }
    },

    //   点击登录的回调
    async onSubmit() {
      let res = await this.$request(
        "/educenter/member/login",
        this.login,
        "post",
        "params"
      );
      console.log(res);
      if (res.status == 200 && res.data.success) {
        // 登陆成功
        // 将用户信息保存至vuex
        this.$store.commit("updateUserInfo", res.data.data.mem);

        // 将返回的用户信息保存至localstorage中
        window.localStorage.setItem(
          "userInfo",
          JSON.stringify(res.data.data.mem)
        );

        // 将token存入本地
        window.localStorage.setItem("token", res.data.data.token);

        //   跳转至主界面
        this.$router.push("/index");
      } else if (res.status == 200 && !res.data.success) {
        this.$message.warning("登录失败,账号或密码错误!");
      }
    },

    handleClick(e) {
      console.log(e.name);
    },

    // 获取验证码
    async getCode() {
      this.isCountDownShow = true;
      let res = await this.$request(
        `/edumsm/msm/send/${this.registered.mobile}`
      );
      console.log(res);
      if (res.data.success) {
        this.startCountDown();
      }
    },

    // 倒计时
    startCountDown() {
      this.countDownSecond = 60;
      timer = setInterval(() => {
        this.countDownSecond--;
        if (this.countDownSecond == 0) {
          clearInterval(timer);
          this.isCountDownShow = false;
        }
      }, 1000);
    },
    // 点击注册的回调
    async clickRegistered() {
      let res = await this.$request(
        "/educenter/member/register",
        this.registered,
        "post",
        "params"
      );
      console.log(res);
      // 如果注册成功，清空所有数据并跳转至登录界面，自动填写手机号码
      if (res.data.success) {
        this.$message.success("注册成功!");
        this.login.mobile = this.registered.mobile;
        this.activeName = "first";
        this.registered = {
          mobile: "",
          password: "",
          code: "",
          nickname: "",
          avatar: null,
        };
      } else {
        this.$message.error("注册失败,请稍后重试!");
      }
    },
  },
};
</script>

<style scoped>
.login {
  background-color: #ecefff;
  height: 100vh;
}

.logoContainer {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  justify-content: center;
}

.logo {
  width: 50px;
}

.logo img {
  width: 100%;
}

.name {
  color: #2b2525;
  font-size: 20px;
  letter-spacing: 4px;
  font-weight: bold;
  margin-left: 7px;
}

.main {
  width: 350px;
  height: 400px;
  position: absolute;
  right: 10vw;
  top: 15vh;
}

.mainBox {
  width: 350px;
  background-color: #fff;
  height: 330px;
  border-radius: 10px;
  overflow: hidden;
}

.mainBoxRegistered {
  height: 430px;
}

.el-form /deep/ .el-form-item__content {
  margin: 0 !important;
  padding: 0 20px;
}

.el-input /deep/ input {
  border-radius: 10px;
}

.loginInput {
  margin-top: 20px;
}

.el-tabs /deep/ .is-active,
.el-tabs /deep/ div:hover {
  color: #0ea1d6;
}

.el-tabs /deep/ .is-active {
  background-color: #fff;
}

.el-tabs /deep/ .el-tabs__item {
  border: none !important;
  font-size: 18px;
  height: 50px;
  line-height: 50px;
}

.el-tabs /deep/ .el-tabs__nav {
  border: none;
}

.el-tabs /deep/ .el-tabs__nav-scroll {
  background-color: #f5f5f6;
}

.el-input /deep/ .el-input__inner {
  height: 48px;
  font-size: 15px;
}

.el-button {
  width: 100%;
  background-color: #0ea1d6;
  border: none;
  border-radius: 10px;
  margin-top: 10px;
  height: 45px;
  font-size: 15px;
}

.el-button:hover {
  background-color: #11c0d7;
}

.codeContainer {
  position: relative;
}

.codeButtonContainer {
  position: absolute;
  top: 50%;
  right: 30px;
  transform: translateY(-50%);
}

.getcode {
  background-color: #11c0d7;
  color: white;
  height: 35px;
  margin: 0;
}

.countDown {
  color: rgb(141, 141, 141);
}

.el-progress {
  width: 160%;
}
.strength {
  font-size: 13px;
  color: #271e25;
  position: relative;
  top: 5px;
  left: 0;
  transition: 0.5s all ease;
}
</style>
