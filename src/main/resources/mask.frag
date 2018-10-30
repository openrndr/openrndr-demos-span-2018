#version 330

in vec2 v_texCoord0;
uniform sampler2D tex0;
uniform sampler2D tex1;

out vec4 o_color;
void main() {
    vec4 imageC = texture(tex0, v_texCoord0);
    vec4 maskC = texture(tex1, v_texCoord0);
    o_color = imageC * maskC.r;
}