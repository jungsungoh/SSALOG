<template>
  <v-row justify="center">
    <v-dialog v-model="dialog" fullscreen hide-overlay transition="dialog-bottom-transition">
      <v-card>
        <v-toolbar dark color="primary">
          <v-btn icon dark @click="closeDialog()">
            <v-icon>mdi-close</v-icon>
          </v-btn>
          <v-toolbar-title>프로필 수정</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-toolbar-items>
            <v-btn dark text @click="closeDialog()">저장하기</v-btn>
          </v-toolbar-items>
        </v-toolbar>
        <v-list three-line subheader>
          <v-file-input
            v-model="imgFile"
            label="File input"
            accept="image/png, image/jpeg, image/jpg"
          ></v-file-input>
          <v-subheader>User Controls</v-subheader>
          <v-list-item>
            <v-list-item-content>
              <v-list-item-title>Content filtering</v-list-item-title>
              <v-list-item-subtitle
                >Set the content filtering level to restrict apps that can be
                downloaded</v-list-item-subtitle
              >
            </v-list-item-content>
          </v-list-item>
          <v-list-item>
            <v-list-item-content>
              <v-list-item-title>Password</v-list-item-title>
              <v-list-item-subtitle
                >Require password for purchase or use password to restrict
                purchase</v-list-item-subtitle
              >
            </v-list-item-content>
          </v-list-item>
        </v-list>
        <v-divider></v-divider>
        <v-list three-line subheader>
          <v-subheader>General</v-subheader>
          <v-list-item>
            <v-list-item-action>
              <v-checkbox v-model="notifications"></v-checkbox>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title>Notifications</v-list-item-title>
              <v-list-item-subtitle
                >Notify me about updates to apps or games that I downloaded</v-list-item-subtitle
              >
            </v-list-item-content>
          </v-list-item>
          <v-list-item>
            <v-list-item-action>
              <v-checkbox v-model="sound"></v-checkbox>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title>Sound</v-list-item-title>
              <v-list-item-subtitle
                >Auto-update apps at any time. Data charges may apply</v-list-item-subtitle
              >
            </v-list-item-content>
          </v-list-item>
          <v-list-item>
            <v-list-item-action>
              <v-checkbox v-model="widgets"></v-checkbox>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title>Auto-add widgets</v-list-item-title>
              <v-list-item-subtitle>Automatically add home screen widgets</v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
        </v-list>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
import axios from "axios";
import { mapState } from "vuex";

export default {
  props: {
    dialog: {
      default: false
    }
  },
  data() {
    return {
      notifications: false,
      sound: true,
      widgets: false,
      imgFile: null
    };
  },
  computed: mapState(["ServerURL", "username", "accessToken"]),
  methods: {
    closeDialog() {
      this.$emit("update:dialog", false);
    }
  },
  watch: {
    async imgFile(file) {
      if (file !== undefined) {
        const formData = new FormData();
        formData.append("filename", file);
        formData.append("username", this.username);
        try {
          await axios.post(`${this.ServerURL}/user/upload_profile_img`, formData, {
            headers: {
              "Content-Type": "multipart/form-data",
              Authorization: `Bearer ${this.accessToken}`
            }
          });
          // 업로드 했으면 다시 get 으로 요청해서 header 이미지 새로고침
          await this.$store.dispatch("Thumbnail", this.username);
        } catch (err) {
          console.error(err);
        }
      }
    }
  }
};
</script>
